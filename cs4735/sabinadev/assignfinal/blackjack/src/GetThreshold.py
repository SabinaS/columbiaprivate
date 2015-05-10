'''
Created on May 10, 2015

@author: sabinasmajlaj
'''

import math
from operator import itemgetter

import cv2
import numpy as np
from VisualBlackjack import VisualBlackjack 

CARD_VALUE = 10

class GetThreshold():
    
    def getThreshold(self):
        # Initialize green boundary
        thresh_value = ([0, 175, 0], [255, 255, 255])
        
        # Open the webcam and read an image
        vb = VisualBlackjack()
        cap = cv2.VideoCapture(0)
        if cap.isOpened():
            ret, frame = cap.read()
        else:
            cap.open()
            ret, frame = cap.read()
        frame_buffer = []
        movement_buffer = []
        while (ret<4):
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
        
        # Get number of card value of that image
        card_val= vb.readImage(frame, thresh_value)
        
        # Continuously compare to CARD_VALUE
        # If we still haven't gotten 10 or our green is above 230, don't keep going
        keep_going = True
        while(keep_going): 
            if(card_val != CARD_VALUE):
                if(thresh_value[0][1] > 230):
                    keep_going = False
                else: 
                    new_thresh = thresh_value[0][1] + 10
                    thresh_value[0][1] = new_thresh
                    card_val= vb.readImage(frame, thresh_value)
            else:
                keep_going = False
        return thresh_value
        
    def __init__(self):
        return None