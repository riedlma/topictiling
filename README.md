# TopicTiling

Topic Tiling is a LDA based Text Segmentation algorithm. 
This algorithm is based on the well-known TextTiling 
algorithm, and segments documents using the Latent 
Dirichlet Allocation (LDA) topic model. TopicTiling performs 
the segmentation in linear time and thus is computationally 
less expensive than other LDA-based segmentation methods. 

For the LDA computation we use [JGibbLda](http://jgibblda.sourceforge.net/) and modified it slightly, making this project to be licenced under GPL.


Table of Content
================


  * [Usage of the binaries](#usage-of-the-binaries)
  * [Usage of the source code](#usage-of-the-source-code)
  * [Citation](#citation)
  * [License](#license)




Usage of the binaries
===============

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

You can download the an executable topictiling.zip which can be executed from commandline (using the topictiling.sh script) and outputs the text and the suggested boundaries. The parameters of the script are shown when just executing the shell script:

```
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
 [java]  -tmd VAL : Directory of the topic model (GibbsLDA should be used)
 [java]  -tmn VAL : Name of the topic model (GibbsLDA should be used)
 [java]  -w N     : Window size used to calculate the sentence similarity
```

The parameters -fp, -fd, -tmd, -tmn are the ones that have to be specified and –ri should be parametrized by using about 5 repeated inferences.

For the algorithms it’s important to have a trained LDA model. The model should be in a similar domain as the data you apply my algorithm. You have to train it yourself using GibssLda++ or JGibbslda (http://gibbslda.sourceforge.net/) . They both have the same output format. The output of my algorithms is in xml format:

```
<document>
<documentName>…</documentName>
<segment>
<depthScore>score<depthScore>
<text>…</text>
</segment>
…

</document>
```

The code returns all maxima where a boundary might be set. If the number of segments should be given use the N highest depthScore values. 


Usage of the source code
===============
Import both projects into Eclipse. The LDA project contains JGibbLda with slightly modifications, so the mode method can be computed. Additionally it contains UIMA Annotators, so it can be used within a UIMA Pipeline. The project also has dependencies to DKPro and uimafit. To run the TopicTiling algorithm, execute the class TopicTilingTopicDocument. 

Citation
===============
If you use SECOS, please cite one of the following papers/article:

```

@inproceedings{riedl12_jlcl,
	author = {Martin Riedl and Chris Biemann},
	title = {TopicTiling: A Text Segmentation Algorithm based on LDA},
	year = {2012},
	address = {Jeju, Republic of Korea},
	booktitle = {Proceedings of the Student Research Workshop of the 50th Meeting of the Association for
               Computational Linguistics},
	pages = {37--42},
	url={www.jlcl.org/2012_Heft1/jlcl2012-1-3.pdf},
}

@inproceedings{riedl12_naacl,
	author = {Riedl, Martin and Biemann, Chris},
	title = {How Text Segmentation Algorithms Gain from Topic Models},
	year = {2012},
	address = {Montreal, Canada},
	booktitle = {Proceedings of the Conference of the North American Chapter of the
               Association for Computational Linguistics: Human Language Technologies},
	series={NAACL-HLT 2012},
	url={http://www.aclweb.org/anthology/N12-1064},
	pages={553--557 }
}
```



License
===============
TopicTiling is a free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation.

TopicTiling is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.



