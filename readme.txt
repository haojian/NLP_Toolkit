/**************
Notes for personal record.
***************/


/********************/
Sentence Splitting:
From the paper: http://ufal.mff.cuni.cz/pbml/98/art-marsik-bojar.pdf,
There is a comparison between different sentence splitting frameworks.
I used the Apache OpenNLP in this project.

sentence splitter DescriptiveStatistics:
n: 1058041
min: 1.0
max: 50.0
mean: 13.857152983674476
std dev: 8.803619413723986
median: 12.0
skewness: 1.0384383831028539
kurtosis: 1.1811509404259999
/********************/


/********************/
BootStrapping:

Template Extraction:

Sample Template:
Everything was  [VALUE] , from the  [ATTRIBUTE]  to the wine to the service. 
Everything was  [VALUE] , from the food to the wine to the  [ATTRIBUTE] . 

Pseudo code:

dist = closest distance between [VALUE] & [ATTRIBUTE] (most compact template).
if(dist < threshold){
	the whole sentence would be template.
}else{
	the template would be the words that occur between this pair.
}

/********************/



