 ----------------------------------------------------
|                     TopicTiling                    |
 ----------------------------------------------------

Topic Tiling is a LDA based Text Segmentation algorithm. 
This algorithm is based on the well-known TextTiling 
algorithm, and segments documents using the Latent 
Dirichlet Allocation (LDA) topic model. TopicTiling performs 
the segmentation in linear time and thus is computationally 
less expensive than other LDA-based segmentation methods. 

USE:

The tool has been developed and tested using unix-based systems.
As TopicTiling is written in Java it should also run on Windows
machines. For executing TopicTiling you have to uncompress the 
zip file and execute the topictiling.sh (Unix-based system) or 
topictiling.bat (Windows-based system). The output is given in 
an XML format with suggested topical boundaries.

HINT FOR NON-LATIN LANGUAGES:
If you want to process e.g. Chinese, Arabic languages with TopicTiling
you have to provide tokenized text (both for TopicTiling and GibbsLDA)
and in addition use the flag -s which disables the Stanford tokenization
and uses instead a simple whitespace tokenizer that expects one sentence
per line


The parameters of the script are shown when no parameters are given: 

 [java] Option "-fd" is required
 [java] java -jar myprogram.jar [options...] arguments...
 [java]  -dn      : Use the direct neighbor otherwise the highest neighbor will be used
 [java]             (default false)
 [java]  -fd VAL  : Directory fo the test files
 [java]  -fp VAL  : File pattern for the test files
 [java]  -i N     : Number of inference iterations used to annotate words with topic
 [java]             IDs (default 100)
 [java]  -m       : Use mode counting (true/false) (default=true)
 [java]  -out VAL : File the content is written to (otherwise stdout will be used)
 [java]  -ri N    : Use the repeated inference method
 [java]  -rs N    : Use the repeated segmentation
 [java]  -s       : Use simple segmentation (default=false)
 [java]  -tmd VAL : Directory of the topic model (GibbsLDA should be used)
 [java]  -tmn VAL : Name of the topic model (GibbsLDA should be used)
 [java]  -w N     : Window size used to calculate the sentence similarity

The parameters -fp, -fd, -tmd, -tmn are the ones that have to be specified 
and –ri should be parametrized by using about 5 repeated inferences.

For the algorithms it’s important to have a trained LDA model. The model should 
be in a similar domain as the data you apply my algorithm. You have to train it 
yourself using GibssLda++ or JGibbslda (http://gibbslda.sourceforge.net/) . They 
both have the same output format. The output of the algorithms is given in XML
and looks like:

<document>
<documentName>…</documentName>
<segment>
<depthScore>score<depthScore>
<text>…</text>
</segment>
…

</document>

The code returns all possible boundary positions (all maxima). If the number of 
segments is known, select the the N highest depthScore values as boundary positions.
 

LICENSE:

The software is released under GPL 3.0

PAPERS:


    Riedl, M., Biemann, C. (2012): Text Segmentation with Topic Models. Journal for Language Technology and Computational Linguistics (JLCL), Vol. 27, No. 1, pp. 47--70, August 2012 (pdf)
    Riedl M., Biemann C. (2012): How Text Segmentation Algorithms Gain from Topic Models, Proceedings of the Conference of the North American Chapter of the Association for Computational Linguistics: Human Language Technologies (NAACL-HLT 2012), Montreal, Canada. (pdf)
    Riedl M., Biemann C. (2012): TopicTiling: A Text Segmentation Algorithm based on LDA, Proceedings of the Student Research Workshop of the 50th Meeting of the Association for Computational Linguistics, Jeju, Republic of Korea. (pdf)
    Riedl M., Biemann C. (2012): Sweeping through the Topic Space: Bad luck? Roll again! In Proceedings of the Joint Workshop on Unsupervised and Semi-Supervised Learning in NLP held in conjunction with EACL 2012, Avignon, France (pdf)
    
