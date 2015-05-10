'''
Created on May 4, 2015

@author: Kevin Walters
'''

from Cards import Cards

STAND = 0
HIT = 1
OVER = 2
RESULTS = ["STAND", "HIT", "OVER"]

class BlackjackLogic(object):
    
    def getDecision(self):
        if self.card_sum + self.num_ace > 21:
            return STAND
        if self.card_sum > 16:
            return STAND
        if self.card_sum < 12 and self.num_ace == 0:
            return HIT
        if self.card_sum < 17 and self.num_ace == 0:
            if self.dealer_card in [Cards.TWO, Cards.THREE, Cards.FOUR, Cards.FIVE, Cards.SIX]:
                return STAND
            else:
                return HIT
        if self.num_ace == 1:
            if self.card_sum < 7:
                return HIT
            if self.card_sum == 7 and self.dealer_card in [Cards.NINE, Cards.TEN, Cards.JACK, Cards.QUEEN, Cards.KING]:
                return HIT
            if self.card_sum > 7:
                return STAND
        # More than once Ace, need to first check if in a good position
        # If not, simulate a one-ace scenario recursively
        if self.num_ace > 1:
            tmp_sum = self.card_sum + 11 + self.num_ace - 1
            if tmp_sum > 16 and tmp_sum <= 21:
                return STAND
            tmp_sum = self.card_sum + self.num_ace
            if tmp_sum > 16 and tmp_sum <= 21:
                return STAND
            tmp_sum = self.card_sum + self.num_ace - 1
            if tmp_sum <= 21:
                self.num_ace = 1
                self.card_sum = tmp_sum
                return self.getDecision()
            else:
                return OVER
            

    def __init__(self, cards, dealer_card):
        self.card_sum = 0
        self.num_ace = 0
        self.dealer_card = dealer_card
        for card in cards:
            if card == Cards.ACE:
                self.num_ace += 1
            else:
                self.card_sum += Cards.CARD_VALUES[card]