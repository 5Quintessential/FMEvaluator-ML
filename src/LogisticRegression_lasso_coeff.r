library(glmnet)
library(GGally)
library(ggpubr)
theme_set(theme_pubr())

dat = read.csv("zerocashLogisticRegressionData.csv",header = TRUE, colClasses = "factor")
data = dat[23]

y = dat[1]
x = dat[-1]
my_x = data.matrix(x)
my_y = data.matrix(y)

cvfit = cv.glmnet(my_x, my_y, family="binomial", type.measure = "class") #, alpha=1, nlambda=200
plot(cvfit)
fit = glmnet(my_x, my_y, family = "binomial", alpha = 1, lambda = cvfit$lambda.min)# , alpha = 1, lambda = cvfit$lambda
coefi = fit$beta[,1][fit$beta[,1]!=0]
coefi #Lasso

corr <- dat[, c("NNP", "NNPS", "VBN")]
ggpairs(corr,lower = list(continuous = "smooth"))
ggpairs(corr, title="correlation")
ggcorr(corr, method = c("everything", "pearson")) 
ggpairs(dat, columns = 6:10, ggplot2::aes(colour=Feature)) 




# Create data 
data <- data.frame( var1 = 1:100 + rnorm(100,sd=20), v2 = 1:100 + rnorm(100,sd=27), v3 = rep(1, 100) + rnorm(100, sd = 1)) 
data$v4 = data$var1 ** 2 
data$v5 = -(data$var1 ** 2) 

# Check correlations (as scatterplots), distribution and print corrleation coefficient 
ggpairs(data, title="correlogram with ggpairs()") 


ggplot(dat, aes(x = NNP, y = Feature))+
  geom_bar(
    aes(fill = NN), stat = "identity", color = "white",
    position = position_dodge(0.9)
  )+
  facet_wrap(~Feature) + 
  fill_palette("jco")

