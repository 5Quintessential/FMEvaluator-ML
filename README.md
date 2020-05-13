# FMEvaluator-ML
This repository contains all the source files used to reproduce the results of Machine learning based Software Product Line Feature Model (FM) evaluation approach

## Get Started
To run and explore the case study results, copy all the input files to the *src* folder from the respective **input** directories placed in the case study **S<case ID>** folder under the **results** directory.

## Running the case study
Each case study has a separate *.ipynb* file which will load the main *StatisticalFMEvaluator.ipynb* file. The *StatisticalFMEvaluator.ipynb* file includes all the required classes that performs the feature and relations probability predictions.

## NOTE: 
- The *TextExtractor.ipynb* file includes the code for extracting plain text from PDF files. This was used to extract the input data from PDF specification files for all the case studies used in this project.
- If you want to evaluate a new FM, you need to do the following manual steps to ensure the prediction model performs well enough.
     - Uncomment ```print(scores)``` line to see the top scored words from the text corpus and set the ```root_feature_word``` variable.
     - Pause further execution after the data is written to the ```regression data csv file``` and the features are all printed ```print(allFeatures)```. You must manually edit the generated CSV file to mark all the appropriate feature and non-feature phrases based on the features printed by the ```print``` step. This is done to ensure better predictability power for the *Features* predictor.
     - Any new FM that needs to be evaluated must follow the formatting used in the case study input FM files e.g.```FX-Eshop-FM.txt```, where the relations are marked as **M** for *Mandatory*, **O** for *Optional*, **X** for *Xor* etc.
     - All the ```dot``` files are generated using our custom ```java``` code which is included in the ```src``` directory. The heatmaps for the case studies are included in the ```results``` folder as ```.png``` files.
