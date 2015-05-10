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
from GetThreshold import GetThreshold


CORNER_LEFT = 10
CORNER_RIGHT = 70
CORNER_TOP = 25
CORNER_BOTTOM = 115
STATES = ["WAIT_NO_MVMNT", "WAIT_MVMNT"]

'''
Given a card's contour, determines its rank
Order of steps:
    1. Color the entire contour area white
    2. Use this area to get a list of possible corners, using FAST
    3. Determine the actual card corners
    4. Perform a perspective transform, to get a top-down, 500x700 view of the card
    5. Get the rank view from the top-left corner of the card (to speed up template matching)
    6. Use the template matcher on the corner to determine the rank 
'''
def getRank(img, card_contour):
    # Color the contour area white
    white_card = np.zeros(img.shape[:2], np.uint8)
    cv2.drawContours(white_card, [card_contour], 0, 255, -1)
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
    #cv2.imshow('Output.png', card_corner)
    #cv2.waitKey(0)
    #cv2.destroyAllWindows()
    tm = TemplateMatcher()
    card_val = tm.matchTemplate(card_corner)
    print Cards.CARDS[card_val]
    return card_val

'''
Computes the difference between three consecurite grayscale frames. 
Helps to determine if a movement is occuring in the frame
'''
def frame_difference(frames):
    diff1 = cv2.absdiff(frames[2], frames[1])
    diff2 = cv2.absdiff(frames[1], frames[0])
    return cv2.bitwise_and(diff1, diff2)

def readImage(img, thresh_value):
    print img.shape

    # A boundary for any non-green pixel
    #green_boundary = ([0, 230, 0], [255, 255, 255])
    green_boundary = thresh_value
    lower_boundary = np.array(green_boundary[0], dtype="uint8")
    upper_boundary = np.array(green_boundary[1], dtype="uint8")
    # Apply a mask to the image, using the boundaries
    mask = cv2.inRange(img, lower_boundary, upper_boundary)
    no_green = cv2.bitwise_and(img, img, mask = mask)
    kernel = np.ones((4,4), np.uint8)
    no_green = cv2.morphologyEx(no_green, cv2.MORPH_CLOSE, kernel)
    no_green = cv2.morphologyEx(no_green, cv2.MORPH_OPEN, kernel)
    no_green = cv2.morphologyEx(no_green, cv2.MORPH_CLOSE, kernel)
    to_binary = no_green.copy()

    imggray = cv2.cvtColor(to_binary, cv2.COLOR_BGR2GRAY)
    ret, b_img = cv2.threshold(imggray, 0, 255, 0)
    
    cv2.imwrite('Output.png', b_img)

    img2 = b_img.copy()
    cnt, _ = cv2.findContours(b_img, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    print "len: %d" %len(cnt)
    
    '''if len(cnt) < 3:
        print "There are not enough cards on the table."
        print "The dealer should receive one card, and the user two"
        return'''
    hand = []
    dealer = None
    cv = ContourValue()
    for j in range(len(cnt)):
        #print cv2.contourArea(cnt[j])
        if cv2.contourArea(cnt[j]) < 10000:
            continue
        
        new_img = img2.copy()
        
        card_val = cv.getContourValue(img, cnt[j])
        if card_val > 10:
            card_val = getRank(new_img, cnt[j])
        
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
    
    if dealer == None:
        print "Dealer does not have a card. Please place the dealer's card on the top part of the table."
        return
    print "Current hand:", print_hand
    print "Dealer:", Cards.CARDS[dealer]
    
    blackjack = BlackjackLogic(hand, dealer)
    decision = blackjack.getDecision()
    print RESULTS[decision]
    return card_val

if __name__ == '__main__':
    print "Reading: playing_card_5.png"
    img = cv2.imread('playing_card_5.png')
    readImage(img)
    #print "about to read"
    state = 0
    # Get the correct threshold
    gt = GetThreshold()
    thresh_value = gt.getThreshold()
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
        cv2.imshow("out", frame) #testing 
        # Keep track of last 3 grayscale frames seen
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        #cv2.imshow('GRAY.png',gray) #testing 
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
        #print "reading image"
        if state == 0 and len(movement_buffer) == 30 and True not in movement_buffer:
            state = 1
            print "Deciding action..."
            #img = cv2.imread("full_img.jpg")
            readImage(frame, thresh_value)
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