import math

class Factor(object):
    def __init__(self, name, defaultWeight, defaultIndex):
        self.name = name
        self.defaultWeight = defaultWeight
        self.defaultIndex = defaultIndex
        self.currentWeight = defaultWeight
        self.currentIndex = defaultIndex
        self.upperLimit = 1
        self.lowerLimit = 0
        self.normalized_flg = False


class FuzzyI(object):

    def __init__(self):
        self.limit = 1
        self.k = 1
        self.flexibility = 1
        self.delta = 0
        self.deltas = []

    def FuzzyI(self, factors):
        self.flexibility = 1
        self.k = math.fabs(factors[0].defaultWeight - factors[len(factors) - 1].defaultWeight) / math.fabs(
            factors[0].defaultWeight + factors[len(factors) - 1].defaultWeight)
        for i in range(1, len(factors)):
            j = i - 1
            c = 0.0
            while (j >= 0):
                c += math.fabs(factors[i].defaultWeight + factors[j].defaultWeight) / math.fabs(
                    factors[i].defaultWeight - factors[j].defaultWeight)
                j -= 1
            c *= self.k
            current_limit = (1 - factors[i].defaultWeight) / c
            if self.limit > current_limit:
                self.limit = current_limit
        for i in range(0, len(factors) - 1):
            j = i + 1
            c = 0.0
            while (j < len(factors)):
                c += math.fabs(factors[i].defaultWeight + factors[j].defaultWeight) / math.fabs(
                    factors[i].defaultWeight - factors[j].defaultWeight)
                j += 1
            c *= self.k
            current_limit = factors[i].defaultWeight / c
            if self.limit > current_limit:
                self.limit = current_limit
        for i in range(0, len(factors)):
            factors[i].currentWeight = factors[i].defaultWeight
        self.delta = self.limit * self.flexibility
        for i in range(0, len(factors)):
            for j in range(i + 1, len(factors)):
                self.deltas.append(self.k * self.delta * (
                    math.fabs(factors[i].defaultWeight + factors[j].defaultWeight) / math.fabs(
                        factors[i].defaultWeight - factors[j].defaultWeight)))

        for i in range(0, len(factors)):
            for j in range(i, len(factors)):
                if factors[j].currentIndex == i:
                    break
            l = j - 1
            while (l >= i):
                offset = len(factors) * (len(factors) - 1) / 2 - (len(factors) - (l + 1)) * (
                len(factors) - (l + 1) - 1) / 2 - (len(factors) - (j + 1)) - 1
                factors[j].currentWeight += self.deltas[offset]
                factors[l].currentWeight -= self.deltas[offset]
                l -= 1
            holder = factors.pop(j)
            factors.insert(0, holder)
## Below code has error in calculation of lower limit
        i = 0
        while (i < len(factors)):
            UL = factors[i].defaultWeight
            LL = factors[i].defaultWeight
            j = factors[i].defaultIndex
            l = factors[i].defaultIndex - 1
            while (l >= 0):
                offset = (len(factors) * (len(factors) - 1) / 2 - (len(factors) - (l + 1)) * (
                    len(factors) - (l + 1) - 1) / 2 - 1) - (len(factors) - 1 - j)
                UL += self.deltas[offset]
                l -= 1
            factors[i].upperLimit = UL
            l = factors[i].defaultIndex + 1
            offset = len(factors) * (len(factors) - 1) / 2 - ((len(factors) - j) * ((len(factors) - j) - 1) / 2)
            while (l < len(factors)):
                LL -= self.deltas[offset]
                offset -= 1
                l += 1
            factors[i].lowerLimit = LL
            i += 1

        return factors


class Main:
    def __init__(self):
        self.factors = []
        self.createFactors()
        self.normalize()
        self.createPermutation()
        fuzzy = FuzzyI()
        self.factors = fuzzy.FuzzyI(self.factors)
        for factor in self.factors:
            print "\n"
            print "Name=" + factor.name
            print "Current Weight=" + str(factor.currentWeight)
            print "Default Weight=" + str(factor.defaultWeight)

    def createFactors(self):
        self.factors.append(Factor("Placement", 0.39, 0))
        self.factors.append(Factor("Quality_of_Education", 0.27, 1))
        self.factors.append(Factor("Fees", 0.18, 2))
        self.factors.append(Factor("Infrastructure_and_Funding", 0.09, 3))
        self.factors.append(Factor("Location", 0.07, 4))

    def createPermutation(self):
            self.factors[0].currentIndex = 1
            self.factors[1].currentIndex = 0
            self.factors[2].currentIndex = 4
            self.factors[3].currentIndex = 2
            self.factors[4].currentIndex = 3

    def normalize(self):
        length = len(self.factors)
        i = 1
        while (i < length):
            if (self.factors[i - 1].defaultWeight == self.factors[i].defaultWeight):
                length -= 1
                self.factors[i].normalized_flg = True
                self.factors[i].defaultWeight *= 2
                name = " and " + self.factors[i - 1].name
                self.factors[i].name += name
                for j in range(i, len(self.factors)):
                    self.factors[j].defaultIndex -= 1
                self.factors.pop(i - 1)
                self.normalize()
            i += 1


if __name__ == '__main__':
    Main()
