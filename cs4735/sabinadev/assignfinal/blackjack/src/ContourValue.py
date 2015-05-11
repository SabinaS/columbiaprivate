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
    def getContourValue(self, img, cnt, b_thresh):
        cv2.imwrite('Orig.png', img)
        # Black out everything except the card we're dealing with
        mask = np.zeros_like(img)
        cv2.drawContours(mask, [cnt], 0, (255, 255, 255), -1)
        card_img = np.zeros_like(img)
        card_img[mask == (255, 255, 255)] = img[mask == (255, 255, 255)]
        
        cv2.imwrite("Output.png", card_img)
        
        # Binarize the image
        imggray = cv2.cvtColor(card_img, cv2.COLOR_BGR2GRAY)
        
        #print b_thresh
        ret, card_img = cv2.threshold(imggray, b_thresh, 255, 0)
        
        kernel = np.ones((2,2), np.uint8)
        card_img = cv2.erode(card_img, kernel, iterations=1)
        card_img = cv2.dilate(card_img, kernel, iterations=1)
        #if b_thresh == 195:
        #    cv2.imshow('b_out', card_img)
        #    cv2.imwrite("b_out.png", card_img)
        #    cv2.waitKey(0)
        #    cv2.destroyAllWindows()

        #image_canny = cv2.Canny(card_img,100,200) 
        #cv2.imwrite('Canny.png', image_canny)
        
        # Get contours
        contours = self.getContours(card_img)
        #contour_img = cv2.drawContours(img, contours, 3, (0,255,0), 3)
        
        # Get the moments
        moment_list = self.getMoments(contours)
        
        # Reduce the moments
        moment_count = self.getReducedMoments(moment_list)
        
        # Get the final card number value
        card_number = self.getCardNumber(moment_count)
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
        if moment_count == 19:
            extra_contours = 9
        elif moment_count > 15:
            return moment_count
        card_number = moment_count - extra_contours
        return card_number
        
    def __init__(self):
        return None