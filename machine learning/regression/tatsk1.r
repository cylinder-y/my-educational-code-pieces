rm(list=ls())


library(readxl)
library(caret)
library(vegan)

Dealings <- as.data.frame(
  read_excel("d:/University/Machine Learning/Classification.xls", col_names = FALSE)
)

Dealings[Dealings$X__1==-1,]$X__1<-0
table(Dealings$X__1)/sum(table(Dealings$X__1))

y <- as.factor(Dealings$X__1)

colnames(Dealings) <- gsub('__','',colnames(Dealings))

str(Dealings)

#create data set without second column
deals <- Dealings[, -2]
deals <- deals[,-1]

head(deals)

str(deals)

#elements with near Zero dispersion not found, ignoring it
#(nz = nearZeroVar(deals))

#elements with large coleriation
(highCor = findCorrelation(cor(deals), cutoff = 0.75, verbose = TRUE))
print("#Removing of the following columns"); names(deals)[highCor]
highCor
deals.cleaned = deals[,-highCor]
dim(deals.cleaned)

#elements with linear dependency not found, ignoring it
#(linCombo <- findLinearCombos(deals.cleaned))


featurePlot(deals.cleaned, y)



# library(vegan)
# mod.pca <- vegan::rda(deals.cleaned, scale = TRUE)
# ev <- mod.pca$CA$eig
# # ?????????????????????? ???????????????? ??????????????-????????????????
# barplot(ev, col = "bisque", las = 2)
# abline(h = mean(ev), col = "red")
# legend("topright", "?????????????? ?????????????????????? ????????????????", lwd = 1, col = 2, bty = "n")
# 
# important_amount = c(length(ev[ev > mean(ev)]), sum(ev[ev > mean(ev)])/sum(ev))
# 
# set.seed(100)
# prePCA <- preProcess(deals.cleaned,
#                      method = c("center", "scale", "pca"), pcaComp = important_amount[1])
# deals.pca <- predict(prePCA, deals.cleaned)
# train.index <- createDataPartition(y, p = 0.75, times = 1)
# trControl = trainControl(method = "LGOCV", index = train.index
#                           ,verboseIter=TRUE
#                          )
# head(deals.pca)
# (modPCA <- train(deals.pca, y, "rf",
#                  #family = binomial,
#                  trControl = trControl))


#???????????????????????????????????? ??????????????????

set.seed(100)
train.index <- createDataPartition(y, p = 0.75)


trControl = trainControl(
                         method = "cv",
                         # method = "oob",
                         index = train.index,
                         # preProcOptions = list(thresh = 0.95, PCA = 5, k = 5),
                         verboseIter = TRUE
                         )


# library(h2o)
# h2o.init()
dim(deals.cleaned)
(modSource <- train(deals.cleaned, y,
                    # family = binomial,
                    "ada",
                    tuneGrid = expand.grid(nu = c(.1,.05,.15,.2), 
                                           iter = c(50, 100, 150), 
                                           maxdepth=c(1,2,3,4)),
                    # "rf",
                    # tuneGrid = expand.grid(.mtry=seq(1,dim(deals.cleaned)[2])),
                    # "gbm",
                    # tuneGrid = expand.grid(interaction.depth = c(1,2,3),
                    #                        shrinkage = c(.1, .05,.2,.15),
                    #                        n.trees = c(10, 50, 100, 150),
                    #                        n.minobsinnode = c(5,10)),
                    trControl = trControl,
                    preProcess = c('center','scale', 'expoTrans', 'knnImpute')
                    ))
plot(varImp(modSource, scale = FALSE))

#plsRglm -56
#rf -57
#glmnet_h2o
#rfRules - 60% mtry=13 maxdepth=4 slooooooooooooow
#wsrf 59/56% mtry = 2
#ls(getModelInfo(model = "lm"))
#modelLookup("glm")

