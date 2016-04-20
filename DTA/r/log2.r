#!/usr/bin/env Rscript

library(kernlab)

print('Read file list...')
fs <- list.files("../../../DEVHOME/eclipsewspc/DTA/rtrf", pattern = "*feat*")

print('First 6 files to be processed')
head(fs)

tbd = fs[1:1]
tbd[1] <- 'feat.1.csv'
head(tbd)

#mydata <- read.csv("../../../DEVHOME/eclipsewspc/DTA/rtrf/feat.1.csv")
#print('head mydata:')
#head(mydata)
#mylogit <- glm(out ~ sp1+sp2+sp3+sp4+sp5+sp6+sp7+sp8+sp9+sp10+ro1+ro2+ro3+ro4+ro5+ro6+ro7+ro8+ro9+ro10+ac1+ac2+ac3+ac4+ac5+ac6+ac7+ac8+ac9+ac10+ar1+ar2+ar3+ar4+ar5+ar6+ar7+ar8+ar9+ar10+la1+la2+la3+la4+la5+la6+la7+la8+la9+la10+gp1+gp2+gp3+gp4+gp5+gp6+gp7+gp8+gp10+gp11+gp12+gp13+as1+as2+as3+as4+as5+l21+l22+l23+l24+l25+l26+l27+l28+l29+l210+l31+l32+l33+l34+l35+l36+l37+l38+l39+l310+AvgSp+StdSp+IdleR+BeeR+DimR, data = mydata, family = "binomial")
#drvroutes <- mydata[mydata[,"out"]==1,]
#print('head drvroutes:')
#head(drvroutes)
#drvests <- predict(mylogit, drvroutes, type="response")
#outmat <- cbind(drvroutes[,1:2], drvests)
#print('head outmat:')
#head(outmat)

#write.csv(outmat, file="../../../DEVHOME/eclipsewspc/DTA/rtrf/tstest.1.csv")


myfun <- function(filename) {
	mydata <- read.csv(paste("../../../DEVHOME/eclipsewspc/DTA/rtrf/" , filename,sep=""))
	mylogit <- glm(out ~ sp1+sp2+sp3+sp4+sp5+sp6+sp7+sp8+sp9+sp10+ro1+ro2+ro3+ro4+ro5+ro6+ro7+ro8+ro9+ro10+ac1+ac2+ac3+ac4+ac5+ac6+ac7+ac8+ac9+ac10+ar1+ar2+ar3+ar4+ar5+ar6+ar7+ar8+ar9+ar10+la1+la2+la3+la4+la5+la6+la7+la8+la9+la10+gp1+gp2+gp3+gp4+gp5+gp6+gp7+gp8+gp10+gp11+gp12+gp13+as1+as2+as3+as4+as5+l21+l22+l23+l24+l25+l26+l27+l28+l29+l210+l31+l32+l33+l34+l35+l36+l37+l38+l39+l310+g1+g2+g3+g4+g5+g6+g7+g8+g9+g10+AvgSp+StdSp+IdleR+BeeR+DimR, data = mydata, family = "binomial")
	drvroutes <- mydata[mydata[,"out"]==1,]
	summary(mylogit)
	drvests <- predict(mylogit, drvroutes, type="response")
	outmat <- cbind(drvroutes[,1:2], drvests)
	write.csv(outmat, paste("../../../DEVHOME/eclipsewspc/DTA/rtrf/" , gsub("feat","est",filename),sep=""))
	print(c(sum(drvests)))
}

#lapply(tbd, myfun)
lapply(fs, myfun)



