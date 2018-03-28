# TopicTiling

<img src="https://github.com/riedlma/topictiling/raw/master/topictiling.png" alt="TopicTiling" width="400">

Topic Tiling is an LDA-based Text Segmentation algorithm. 
The algorithm is inspired by the well-known [TextTiling](http://www.aclweb.org/anthology/J97-1003) 
algorithm developed by [Marti Hearst](http://people.ischool.berkeley.edu/~hearst/), and segments documents using the Latent 
Dirichlet Allocation (LDA) topic model. TopicTiling performs 
the segmentation in linear time and thus is computationally 
less expensive than other LDA-based segmentation methods. 

I have moved the project from SourceForge to Github. Whereas the code is still the same, I have updated the documentation on this page.

For the LDA computation we use [JGibbLda](http://jgibblda.sourceforge.net/) in a slightly modified version, making this project to be licenced under GPL. 


Table of Content
================


  * [Usage of the binaries](#usage-of-the-binaries)
  * [Usage for non latin languages](#usage-for-non-latin-languages)
  * [Usage of the source code](#usage-of-the-source-code)
  * [Compute a topic model](#compute-a-topic-model)
  * [Citation](#citation)
  * [License](#license)




Usage of the binaries
===============

The tool has been developed and tested using unix-based systems.
As TopicTiling is written in Java it should also run on Windows
machines. 

To start TopicTiling, you have to download the binary ([zip](https://github.com/riedlma/topictiling/releases/download/v1.0/topictiling_v1.0.zip)|[tar.gz](https://github.com/riedlma/topictiling/releases/download/v1.0/topictiling_v1.0.tar.gz)) and decompress the archive. To execute the segmentation method, open the commandline and navigate to the uncompressed folder

```
cd topictiling_v1.0
```

We provide an batch script to start the segmentation for Windows:
```
bash topictiling.bat
```
and a shell script to start the segmentation for unix-based operation systems:
```
sh topictiling.sh
```

These commands will output all parameters of TopicTiling:


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
 [java]  -s       : Use simple segmentation (default=false)
 [java]  -tmd VAL : Directory of the topic model (GibbsLDA should be used)
 [java]  -tmn VAL : Name of the topic model (GibbsLDA should be used)
 [java]  -w N     : Window size used to calculate the sentence similarity
```

In order to test TopicTiling, you also require a topic model that has been computed with either [JGibbLDA](http://jgibblda.sourceforge.net/) or [GibbsLda++](http://gibbslda.sourceforge.net/). Some description for the computation is given [here](#compute-a-topic-model).

Once you have computed a topic model, you might have a folder called *topicmodel* with the following files:
```
topicmodel/model-final.others
topicmodel/model-final.phi
topicmodel/model-final.tassign
topicmodel/model-final.theta
topicmodel/model-final.twords
topicmodel/wordmap.txt
```


For the segmentation, we advise to repeat the inference five times (*-ri 5*) (see [paper](http://www.aclweb.org/anthology/W12-0703)). To start the segmentation, you can then use the following command, considering that the files you want to segment are stored in the folder *files_to_segment* and use as file ending "txt":

```
sh topictiling.sh -ri 5 -tmd topicmodel -tmn mode-final -fp "*txt" -fd files_to_segment
```

The output of the algorithms is in XML format:

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

The code returns all maxima where a boundary might be set. If you know the number of segments, you can just select the N semgents with the highest depthScore scores and ignore the remaining ones. 


Usage for non latin languages
===============
The current version uses the Stanford segmenter for tokenization. However, this tokenizer does not play well on languages without any latin characters (e.g. Chinese, Arabic, Hebrew, Japanese, etc.). In order to segment such languages, segment the texts beforehand and use the parameter *-s* that disables the tokenization and expects all words segmented by white spaces.

Usage of the source code
===============
Import both projects into Eclipse. The LDA project contains JGibbLda with slight modifications, so the mode method can be computed. Additionally it contains UIMA Annotators, so it can be used within a UIMA Pipeline. The project also has dependencies to DKPro and uimafit. To run the TopicTiling algorithm, execute the class TopicTilingTopicDocument. 

Compute a topic model
===============

To compute the topic model with LDA, documents are required that represent the domain of texts, the segmentation method will be applied to. For the computation you can use either [JGibbLDA](http://jgibblda.sourceforge.net/) (written in Java) or the faster C++ version [GibbsLda++](http://gibbslda.sourceforge.net/). To get an impression of the usage of different parameters of LDA you can have a look at our paper: [Sweeping through the Topic Space: Bad luck? Roll again!](http://www.aclweb.org/anthology/W12-0703). In general, we would advise training a topic model with 100 topics, alpha with 50/(number of topics) and alpha equals 0.01.



Citation
===============
If you use TextTiling, please cite one of the following papers/article:

```

@article{Riedl:jlcl,
 author = {Martin Riedl and Chris Biemann},
 title = {{Text Segmentation with Topic Models }},
 journal = {Journal for Language Technology and Computational Linguistics (JLCL)},
year={2012},
  volume={27},
  number={47-69},
  pages={13-24},
  url={http://www.jlcl.org/2012_Heft1/jlcl2012-1-3.pdf}
}

@inproceedings{riedl12_acl,
	author = {Martin Riedl and Chris Biemann},
	title = {TopicTiling: A Text Segmentation Algorithm based on LDA},
	year = {2012},
	address = {Jeju, Republic of Korea},
	booktitle = {Proceedings of the Student Research Workshop of the 50th Meeting of the Association for
               Computational Linguistics},
	pages = {37--42},
	url={http://www.aclweb.org/anthology/W12-3307},
}

```



License
===============
As JGibbLDA is published under GPL 2.0 license, which is contained in the current repository, I had to license via this license.

TopicTiling is a free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation.

TopicTiling is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.



