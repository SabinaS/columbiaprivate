'''
Created on Apr 27, 2015

@author: Kevin Walters
'''

import cv2
from Cards import Cards

class TemplateMatcher():
    
    TEMPLATES = ["k_template.png",
                "j_template.png",
                "q_template.png"]
    
    SUIT_TEMPLATES = ["heart_template.png",
                      "spade_template.png",
                      "club_template.png",
                      "diamond_template.png"]
    
    def matchTemplate(self, card_img):
        min_template = None
        min_score = float("inf")
        # Iterate through templates, find the best match (lowest score for TM_SQDIFF)
        for template in self.TEMPLATES:
            tmp = cv2.imread(template)
            tmp = cv2.cvtColor(tmp, cv2.COLOR_BGR2GRAY) # Convert to a single channel
            result = cv2.matchTemplate(tmp, card_img, cv2.TM_SQDIFF)
            min_val, max_val, min_loc, max_loc = cv2.minMaxLoc(result)            
            if min_val <= min_score:
                min_score = min_val
                min_template = template
        
        # Return the best match
        if "j" in min_template:
            return Cards.JACK
        if "q" in min_template:
            return Cards.QUEEN
        if "k" in min_template:
            return Cards.KING
        
    def matchSuitTemplate(self, card_img):
        min_template = None
        min_score = float("inf")
        # Iterate through templates, find the best match (lowest score for TM_SQDIFF)
        for template in self.SUIT_TEMPLATES:
            tmp = cv2.imread(template)
            tmp = cv2.cvtColor(tmp, cv2.COLOR_BGR2GRAY) # Convert to a single channel
            result = cv2.matchTemplate(card_img, tmp, cv2.TM_SQDIFF)
            min_val, max_val, min_loc, max_loc = cv2.minMaxLoc(result)            
            if min_val <= min_score:
                min_score = min_val
                min_template = template
        
        # Return the best match
        if "heart" in min_template:
            return Cards.HEARTS
        if "spade" in min_template:
            return Cards.SPADES
        if "club" in min_template:
            return Cards.CLUBS
        if "diamond" in min_template:
            return Cards.DIAMONDS
    
    def __init__(self):
        return None