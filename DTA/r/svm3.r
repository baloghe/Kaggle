#!/usr/bin/env Rscript

library(kernlab)

print('Read file list...')
fs <- list.files("../../../DEVHOME/eclipsewspc/DTA/rtrf", pattern = "*feat*")

head(fs)

tbd = fs[1:3]
tbd[1] <- 'feat.1.csv'
head(tbd)

myfun <- function(filename) {
	mydata <- read.csv(paste("../../../DEVHOME/eclipsewspc/DTA/rtrf/" , filename,sep=""))
	#svp <- ksvm( round( (out-0.5) * 2) ~ sp1+sp2+sp3+sp4+sp5+sp6+sp7+ro1+ro2+ro3+ro4+ro5+ro6+ro7+ac1+ac2+ac3+ac4+ac5+ac6+ac7+ar1+ar2+ar3+ar4+ar5+ar6+ar7+la1+la2+la3+la4+la5+la6+la7+gp1+gp2+gp3+gp4+gp5+gp6+gp7+gp8+gp9+gp10+gp11+gp12+gp13+as1+as2+as3+as4+as5+l21+l22+l23+l24+l25+l26+l27+l31+l32+l33+l34+l35+l36+l37+g1+g2+g3+g4+g5+g6+g7+AvgSp+StdSp+IdleR+BeeR+DimR,data=mydata,type="C-svc",kernel='rbfdot',kpar=list(sigma=0.06),C=1)
	svp <- ksvm( round( (out-0.5) * 2) ~ gp1+gp2+gp3+gp4+gp5+gp6+gp7+gp8+gp9+gp10+gp11+gp12+gp13+g1+g2+g3+g4+g5+g6+g7+g8+g9+g10,data=mydata,type="C-svc",kernel='rbfdot',kpar=list(sigma=0.8),C=10)
	drvroutes <- mydata[mydata[,"out"]==1,]
	#drvests <- (1+predict(svp , drvroutes[,4:82], type="response"))/2
	drvests <- (1+predict(svp , drvroutes[,c("gp1","gp2","gp3","gp4","gp5","gp6","gp7","gp8","gp9","gp10","gp11","gp12","gp13","g1","g2","g3","g4","g5","g6","g7","g8","g9","g10")], type="response"))/2
	outmat <- cbind(drvroutes[,1:2],drvests)
	write.csv(outmat, paste("../../../DEVHOME/eclipsewspc/DTA/rtrf/" , gsub("feat","est",filename),sep=""))
	print(c(sum(drvests)))
}

lapply(fs, myfun)
