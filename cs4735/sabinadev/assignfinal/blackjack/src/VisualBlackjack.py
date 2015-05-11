'''
Created on Apr 27, 2015

@author: Kevin Walters
'''


import math
from operator import itemgetter

import cv2

from Cards import Cards
from TemplateMatcher import TemplateMatcher
import numpy as np
from BlackjackLogic import BlackjackLogic
from BlackjackLogic import RESULTS
from ContourValue import ContourValue
from argparse import ArgumentParser


CORNER_LEFT = 0
CORNER_RIGHT = 75
CORNER_TOP = 5
CORNER_BOTTOM = 115
SUIT_TOP = 100
SUIT_BOTTOM = 170
SUIT_LEFT = 0
SUIT_RIGHT = 70
STATES = ["WAIT_NO_MVMNT", "WAIT_MVMNT"]
CARD_VALUE = 10

'''
Given a card's contour, determines its rank
Order of steps:
    1. Color the entire contour area white
    2. Determine the card corners from the contours
    3. Perform a perspective transform, to get a top-down, 500x700 view of the card
    4. Get the rank view from the top-left corner of the card (to speed up template matching)
    5. Use the template matcher on the corner to determine the rank 
'''
def getRank(img, card_contour, b_thresh):
    # Color the contour area white
    mask = np.zeros_like(img)
    cv2.drawContours(mask, [card_contour], 0, (255, 255, 255), -1)
    card_img = np.zeros_like(img)
    card_img[mask == (255, 255, 255)] = img[mask == (255, 255, 255)]
    
    cv2.imwrite("Output.png", card_img)
    
    # Binarize the image
    imggray = cv2.cvtColor(card_img, cv2.COLOR_BGR2GRAY)
    ret, img = cv2.threshold(imggray, b_thresh, 255, 0)
    dists = []
    
    # Get the centroid of the contour
    rect = cv2.minAreaRect(card_contour)
    rect_width = min([rect[1][0], rect[1][1]])
    M = cv2.moments(card_contour)
    cx = int(M['m10']/M['m00'])
    cy = int(M['m01']/M['m00'])
    
    # Get each possible corner's distance from the center
    for point in card_contour:
        point = point[0]
        dist = math.hypot(point[0] - cx, point[1] - cy)
        dists.append((point, dist))
    dists = sorted(dists, reverse=True, key=itemgetter(1))
    
    # Determine which corners are actually corners
    corners = []
    corners.append(dists[0])
    dists.pop(0)
    for dist in dists:
        if len(corners) == 4:
            break
        brk = False
        for corner in corners:
            this_dist = math.hypot(corner[0][0] - dist[0][0], corner[0][1] - dist[0][1])
            # Should not add - break, and don't add to corners
            if this_dist < 0.7*rect_width:
                brk = True
                break
        if not brk:
            corners.append(dist)
                
    if len(corners) != 4:
        return None, None
    corners = [c[0] for c in corners]

    # Order of points: [LR, LL, UL, UR]
    corners = sorted(corners, key=itemgetter(1)) # Sort on "highest" y value
    if corners[0][0] < corners[1][0]: #Highest corner is to left of 2nd highest 
        UL = corners[0]
        UR = corners[1]
    else:
        UL = corners[1]
        UR = corners[0]
    if corners[2][0] < corners[3][0]:
        LL = corners[2]
        LR = corners[3]
    else:
        LL = corners[3]
        LR = corners[2]
        
    src_points = [LR, LL, UL, UR]
    src_points = np.float32(src_points)
    target_points = np.float32([[499, 699],[0, 699], [0, 0], [499, 0]])
    
    # Transform the image into a 500x700 top-down view
    M = cv2.getPerspectiveTransform(src_points, target_points)
    output = cv2.warpPerspective(img, M, (500, 700))

    # Perform template matching on the corner of the card
    card_corner = output[CORNER_TOP:CORNER_BOTTOM, CORNER_LEFT:CORNER_RIGHT]
    
    card_suit = output[SUIT_TOP:SUIT_BOTTOM, SUIT_LEFT:SUIT_RIGHT]

    tm = TemplateMatcher()
    card_val = tm.matchTemplate(card_corner)
    #print Cards.CARDS[card_val]
    #card_suit = tm.matchSuitTemplate(card_suit)
    return card_val#, card_suit

'''
Computes the difference between three consecurite grayscale frames. 
Helps to determine if a movement is occuring in the frame
'''
def frame_difference(frames):
    diff1 = cv2.absdiff(frames[2], frames[1])
    diff2 = cv2.absdiff(frames[1], frames[0])
    return cv2.bitwise_and(diff1, diff2)

def readImage(img, green_thresh, b_thresh, calibrate=0):
    # A boundary for any non-green pixel
    green_boundary = ([0, green_thresh, 0], [255, 255, 255])
    #green_boundary = thresh_value
    lower_boundary = np.array(green_boundary[0], dtype="uint8")
    upper_boundary = np.array(green_boundary[1], dtype="uint8")
    # Apply a mask to the image, using the boundaries
    mask = cv2.inRange(img, lower_boundary, upper_boundary)
    no_green = cv2.bitwise_and(img, img, mask = mask)
    
    kernel = np.ones((1,1), np.uint8)
    no_green = cv2.morphologyEx(no_green, cv2.MORPH_CLOSE, kernel)
    no_green = cv2.morphologyEx(no_green, cv2.MORPH_OPEN, kernel)
    no_green = cv2.morphologyEx(no_green, cv2.MORPH_CLOSE, kernel)
    to_binary = no_green.copy()

    imggray = cv2.cvtColor(to_binary, cv2.COLOR_BGR2GRAY)
    ret, b_img = cv2.threshold(imggray, 0, 255, 0)
    
    cv2.imwrite("Output.png", b_img)

    cnt, _ = cv2.findContours(b_img, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    #print "len: %d" %len(cnt)
    #print "thresh_value: %d" %thresh_value[0][1] 
    
    if calibrate == 1:
        return len(cnt)
    
    if len(cnt) < 3 and calibrate == 0:
        print "There are not enough cards on the table."
        print "The dealer should receive one card, and the user two"
        return
    hand = []
    dealer = None
    cv = ContourValue()
    for j in range(len(cnt)):
        if cv2.contourArea(cnt[j]) < 10000:
            #what happens here??
            continue

        card_val = cv.getContourValue(img, cnt[j], b_thresh)
        #print card_val
        if card_val > 10:
            #print "Card value: %d" %card_val
            card_val = getRank(img, cnt[j], b_thresh)
            
        
        # TODO card_suit in above
        # TODO get card_suit if <= 10 also
        
        
        
        M = cv2.moments(cnt[j])
        cy = int(M['m01']/M['m00'])
        if cy < 0.4*img.shape[0]:
            isDealer = True
        else:
            isDealer = False
        if not card_val and not isDealer:
            print "Could not identify card corners. Unable to identify face card. Please make cards more visible."
            return      
        if not isDealer:
            hand.append(card_val)
        else:
            dealer = card_val
            
    print_hand = [Cards.CARDS[c] for c in hand]
    
    if dealer == None and calibrate == 0:
        print "Dealer does not have a card. Please place the dealer's card on the top part of the table."
        return
    if calibrate == 0:
        print "Current hand:", print_hand
        print "Dealer:", Cards.CARDS[dealer]
        
        blackjack = BlackjackLogic(hand, dealer)
        decision = blackjack.getDecision()
        print RESULTS[decision]
    return card_val

def getThreshold():
        # Open the webcam and read an image
        print "Calibrating thresholds..."
        cap = cv2.VideoCapture(0)
        if cap.isOpened():
            ret, frame = cap.read()
        else:
            cap.open()
            ret, frame = cap.read()
        frame_buffer = []
        movement_buffer = []
        while ret:
            cv2.imshow("out", frame)
            # Keep track of last 3 grayscale frames seen
            gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            if len(frame_buffer) == 3:
                frame_buffer.pop(0)
            frame_buffer.append(gray)
            if len(frame_buffer) < 3:
                ret, frame = cap.read()
                continue
            # Determine if there has been significant movement based on the difference amongst the last 3 frames
            diff = frame_difference(frame_buffer)
            movement = False
            for row in diff:
                if max(row) > 45:
                    movement = True
                    break
            # Keep track of if there were movements in the last 30 frames or not
            if len(movement_buffer) == 30:
                movement_buffer.pop(0)
            movement_buffer.append(movement)
            
            if True not in movement_buffer and len(movement_buffer) == 30:
                break
            
            if cv2.waitKey(1) & 0xFF == ord('q'):
                break
            ret, frame = cap.read()
        
        #frame = cv2.imread("test1.jpg")
        
        # Increase the contrast slightly to help with differentiating (lights lighter, darks darker)
        for i in range(len(frame)):
            for j in range(len(frame[i])):
                for k in range(len(frame[i][j])):
                    val = frame[i][j][k]
                    if val > 127:
                        frame[i][j][k] = min(val + 2, 255)
                    else:
                        frame[i][j][k] = max(val - 2, 0)
        
        # First, get threshold for green background
        green_thresh = 100
        valid_green = []
        while green_thresh < 255:
            ret_val = readImage(frame, green_thresh, 255, calibrate=1)
            print "ret_val: %d" %ret_val 
            if ret_val != 1:
                green_thresh += 5
            else:
                valid_green.append(green_thresh)
                green_thresh += 5
        if len(valid_green) == 0:
            print "Cannot identify card outlines. Please find better lighting."
            return None, None
        print "Green:", valid_green
        
        # If green threshold is set, find black/white threshold
        g_thresh = None
        for green_thresh in valid_green:
            found = False
            b_thresh = 0
            while b_thresh < 255:
                card_val = readImage(frame, green_thresh, b_thresh, calibrate=2)
                print "card_val: %d" %card_val 
                if card_val != CARD_VALUE:
                    b_thresh += 5
                else:
                    found = True
                    print "Found"
                    g_thresh = green_thresh
                    break
            if found:
                break
        if not found:
            print "Could identify cards, but not card contents. Please find better lighting."
            return g_thresh, None
        print "Black threshold:", b_thresh
        return g_thresh, b_thresh
 
        
        '''
        card_val= readImage(frame, thresh_value, True)
        print "card_val: %d" %card_val
        if card_val == CARD_VALUE:
            return thresh_value
        
        # Continuously compare to CARD_VALUE
        # If we still haven't gotten 10 or our green is above 230, don't keep going
        print "comparing"
        keep_going = True
        while(keep_going): 
            if(card_val != CARD_VALUE):
                if(thresh_value[0][1] > 230):
                    #print "card_val: %d" %card_val
                    keep_going = False
                else: 
                    new_thresh = thresh_value[0][1] + 5
                    thresh_value[0][1] = new_thresh
                    card_val= readImage(frame, thresh_value, True)
            else:
                keep_going = False
        print "done with getThresh"
        return thresh_value
        '''
    
        
        

if __name__ == '__main__':
    state = 0
    
    # Get the correct thresholds (one for green background, one for card contents)
    green_thresh, b_thresh = getThreshold()
    if not green_thresh or not b_thresh:
        exit()
    print "Starting"
    #Start
    cap = cv2.VideoCapture(0)
    if cap.isOpened():
        ret, frame = cap.read()
    else:
        cap.open()
        ret, frame = cap.read()
    frame_buffer = []
    movement_buffer = []
    while ret:
        cv2.imshow("out", frame)
        # Keep track of last 3 grayscale frames seen
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        if len(frame_buffer) == 3:
            frame_buffer.pop(0)
        frame_buffer.append(gray)
        if len(frame_buffer) < 3:
            ret, frame = cap.read()
            continue
        # Determine if there has been significant movement based on the difference amongst the last 3 frames
        diff = frame_difference(frame_buffer)
        movement = False
        for row in diff:
            if max(row) > 45:
                movement = True
                break
        # Keep track of if there were movements in the last 30 frames or not
        if len(movement_buffer) == 30:
            movement_buffer.pop(0)
        movement_buffer.append(movement)
        
        # If waiting for no movement, and there has been no movement, read the image
        if state == 0 and len(movement_buffer) == 30 and True not in movement_buffer:
            state = 1
            print "Deciding action..."
            img = cv2.imread("full_img.jpg")
            readImage(frame, green_thresh, b_thresh)
            #cv2.imshow('frame', frame)
        # If waiting for a movement (already read this hand), and there has been one, enter other state
        elif state == 1 and True in movement_buffer:
            print "Waiting for new hand..."
            state = 0
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
        ret, frame = cap.read()
    cap.release()
    cv2.destroyAllWindows()