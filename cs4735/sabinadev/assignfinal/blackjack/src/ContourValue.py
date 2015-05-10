'''
Created on May 7, 2015

@author: sabinasmajlaj
'''

import math
from operator import itemgetter

import cv2

from Cards import Cards
from TemplateMatcher import TemplateMatcher
import numpy as np
from BlackjackLogic import BlackjackLogic
from BlackjackLogic import RESULTS

class ContourValue():
    
    '''
    Given the original image and a contour of a card, get its rank
    '''
    def getContourValue(self, img, cnt):
        cv2.imwrite('Orig.png', img)
        # Threshold the image to BW
        # A boundary for any non-green pixel
        green_boundary = ([0, 230, 0], [255, 255, 255])
        lower_boundary = np.array(green_boundary[0], dtype="uint8")
        upper_boundary = np.array(green_boundary[1], dtype="uint8")
        
        # Apply a mask to the image, using the boundaries
        mask = cv2.inRange(img, lower_boundary, upper_boundary)
        img = cv2.bitwise_and(img, img, mask = mask)
        #cv2.imwrite('Orig2.png', img)
        
        # Get an image of only this card from this
        mask = np.zeros_like(img) # Create mask where white is what we want, black otherwise
        cv2.drawContours(mask, [cnt], 0, 255, -1) # Draw filled contour in mask
        card_img = np.zeros_like(img) # Extract out the object and place into output image
        card_img[mask == 255] = img[mask == 255]
        
        imggray = cv2.cvtColor(card_img, cv2.COLOR_BGR2GRAY)
        ret, card_img = cv2.threshold(imggray, 0, 255, 0)
        
        # Apply BW and threshold
        #image_HSV = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        #ret, image_thresh = cv2.threshold(image_HSV,100,255,cv2.THRESH_BINARY)
         #cv2.imwrite('Thresh.png', card_img)
        image_canny = cv2.Canny(card_img,100,200) 
        cv2.imwrite('Canny.png', image_canny)
        
        # Get contours
        contours = self.getContours(card_img)
        contour_img = cv2.drawContours(img, contours, 3, (0,255,0), 3)
        #cv2.imwrite('Contours.png', contour_img)
        
        # Get the moments
        moment_list = self.getMoments(contours)
        
        # Reduce the moments
        moment_count = self.getReducedMoments(moment_list)
        print "moment_count: %d" %moment_count
        
        # Get the final card number value
        card_number = self.getCardNumber(moment_count)
        print "card_number %d" %card_number
        return card_number
    
    def getContours(self, img):
        cnt, _ = cv2.findContours(img, cv2.RETR_CCOMP, cv2.CHAIN_APPROX_SIMPLE)
        return cnt
    
    def getMoments(self, contours):
        moment_list = []
        for i in range(0, len(contours)):
            M = cv2.moments(contours[i])
            #testing
            moment_list.append(M) #add the moment to the list
            #print("m10: %d and m00: %d" % (M['m10'], M['m00']))
            #print("m01: %d and m00: %d" % (M['m01'], M['m00']))
            if((M['m10'] !=0) and (M['m01'] !=0) and (M['m00'] !=0)):
                cx = int(M['m10']/M['m00'])
                cy = int(M['m01']/M['m00'])
                #print("x: %d and y: %d" % (cx, cy))
        return moment_list
    
    def getReducedMoments(self, moment_list):
        moment_count = 0
        max_x = 0
        max_y = 0
        numFound = 0
        maxSame = 0
         
        for i in range(0, len(moment_list)):
            numFound = 0
            M = moment_list[i]
            if((M['m10'] !=0) and (M['m01'] !=0) and (M['m00'] !=0)):
                moment_count +=1
                cx = int(M['m10']/M['m00'])
                cy = int(M['m01']/M['m00'])
                if(cx>max_x):
                    max_x = cx
                if(cy>max_y):
                    max_y = cy
                for j in range(0, len(moment_list)):
                    M2 = moment_list[j]
                    if((M2['m10'] !=0) and (M2['m01'] !=0) and (M2['m00'] !=0)): 
                        dx = int(M2['m10']/M2['m00'])
                        dy = int(M2['m01']/M2['m00'])
                        if(i != j):
                            if(self.isSameX(cx, dx) and self.isSameY(cy, dy, max_y)):
                                numFound +=1
                                if(numFound > maxSame):
                                    maxSame = numFound
                                print("cx: %d and dx: %d -- cy: %d and dy: %d found: %d" % (cx, dx, cy, dy, numFound))
            
        # Check for extra contours
        extra_moments = (maxSame -1) *2
        if(extra_moments != 1):
            moment_count = moment_count - extra_moments
        return moment_count
             
         
    def  isSameX(self, x1, x2):
        isSame = False
        pixelDiff = 6
        if(x1>=(x2-pixelDiff) and x1<=(x2+pixelDiff)):
            isSame = True
        if(x2>=(x1-pixelDiff) and x2<=(x1+pixelDiff)):
            isSame = True 
        return isSame
    
    def  isSameY(self, y1, y2, max_y):
        isSame = False
        maxDiff = max_y * 0.12; #diff of 12%
        if(y1>=(y2-maxDiff) and y1<=(y2+maxDiff)):
            isSame = True
        if(y2>=(y1-maxDiff) and y2<=(y1+maxDiff)):
            isSame = True 
        return isSame
      
    def getCardNumber(self, moment_count): 
        extra_contours = 5
        if(moment_count>16):
            extra_contours = 7
        card_number = moment_count - extra_contours
        return card_number
        
    def __init__(self):
        return None
    
    
