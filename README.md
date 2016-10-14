# supWSD
supWSD is a supervised word sense disambiguation system.  The flexible framework of supWSD allows users to combine different preprocessing modules, to select features extractors and choose which classifier to use. SupWSD is very light and has very small memory requirements; it provides a simple xml file to configure the system.

## Files & Directories
Name | Description
------------ | -------------
config | Babelnet configuration folder, containing the properties files.
lib | Babelnet libraries folder, containing all the necessary .jar files.
models |  folder contains the models of the preprocessing components (only openNLP).
resource | folder contains the lexical semantic resources (sense inventories).
src/main/java/it/uniroma1/lcl/supWSD | source code package.
LICENSE | license file.
README | this file.
pom.xml | Maven configuration file.
supWSD.xml | supWSD configuration file.
supWSD.xsd | supWSD.xml schema definition. This file describes the elements in supWSD.xml and verify that each item adheres to the description of the element.

## Install
1. Download the jar file from the site (link will shortly follow)
2. Move the jar to one directory (take *supWSD* for example)
3. Copy the following files & directories into *supWSD* dir: supWSD.xml, supWSD.xsd, config, models, resources.

## Requirement
1. This software requires java 8 (JRE 1.8) or higher version.
2. Since supWSD uses JWNL for accessing WordNet, you must define the path of the Wordnet dictionary. In resources/wndictionary/prop.xml you can find this line ```<param name="dictionary_path" value="dict" />``` : the value "dict" specifies the path of the WordNet dictionary.

## Quick start
Assume the jar file is moved to directory "supWSD".

To train one model, type in a shell open to this directory:  
*java -jar supWSD.jar train supWSD.xml train.xml train.keys*  
**train**  function name.  
**supWSD.xml** configuration file.  
**train.xml** corpus file.  
**train.keys** keys file.

To test one file, type in a shell open to this directory:  
*java -jar supWSD.jar test supWSD.xml test.xml test.keys*  
**test**  function name.  
**supWSD.xml** configuration file.  
**test.xml** test file.  
**test.keys** keys file.

## Configuration
supWSD configuration file allows to tune the entire disambiguation process:

Tag | Attributes | Meaning
------------ | ------------- | -------------
working_directory |  | the location where the results will be saved (models, stats and scores).
parser | mns | dataset parser; supWSD has 5 different parser types: **lexical**, **senseval**, **semeval7**, **semeval13**, **semeval15** and **plain**. You can also implement and integrate a new parser. **MNS** (*Model Name System*) is the path to the file containing the lexelt information of testing instances.
preprocessing |  | within this tag you can set the components to be used in the preprocessing pipeline. For each component you can specify the model to be applied using the **model** attribute. The **simple** component performs string splitting using the value of the model attribute.  You can also implement and integrate a new component using the factory method pattern. If you want to bypass a phase, set the value of the child tag at **none**. 
splitter | model | which component to use for sentence splitting: **stanford**, **open_nlp**, **simple**, **none**.
tokenizer | model |  which component to use for sentence tokenization: **stanford**, **open_nlp**, **penn_tree_bank**, **simple**, **none**.
tagger | model | which component to use for part of speech tagging: **stanford**, **open_nlp**, **simple**, **none**.
lemmatizer | model | which component to use for lemmatization: **stanford**, **jwnl**, **simple**, **none**.
dependency_parser |  model| which component to use for dependency parsing: **stanford**, **none**.
features |  |  
pos_tags | cutoff sequences|
local_collocations | cutoff sequences|  
surrounding_words | cutoff stopwords window|  
syntactic_relations | cache strategy vectors vocab window |  
classifier | | **liblinear** or **libsvm** : the classifier trains a model for each annotated word. The model will be used to classify test instances.
writer |  | **all**: export results to a file; **single**: generate a file for each test instance; **plain**: create a plain file, a sentence for each line with senses and probabilities for disambiguated words.
sense_inventory | dict | the sense inventory used for testing instances: **wordnet**, **babelnet** or **none**. For Wordnet you must set the attribute **dict** and specify the path of the WordNet dictionary.

# License
supWSD and its API are licensed under a Creative Commons Attribution-Noncommercial-Share Alike 3.0 License.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
