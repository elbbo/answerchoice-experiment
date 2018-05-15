# Answerchoice-Experiment
A model for processing the MCScript data set provided by the [SemEval-2018 Task 11: Machine Comprehension using Commonsense Knowledge](https://competitions.codalab.org/competitions/17184).

## Configuration
Different variations of Word Embeddings can be integrated into the model. Please make sure they are in the appropriate directory:
  src/test/resources/embeddings/

For the experiment, several variations of pre-trained Word Embeddings were used. The used files can be obtained here:
  [GloVe](https://nlp.stanford.edu/projects/glove/)
  [Word2Vec](https://drive.google.com/file/d/0B7XkCwpI5KDYNlNUTTlSS21pQmM/edit)
  [fastText](https://github.com/facebookresearch/fastText/blob/master/pretrained-vectors.md)

Some of the used methods are based on Google Web 1T 5-gram data set. Please ensure the files are stored in the directory provided for that purpose:
  src/test/resources/Web1t/
  
The corresponding files can be found here:
  [Google Web 1T](https://catalog.ldc.upenn.edu/ldc2006t13)

The model uses [ND4j](https://nd4j.org/) as computing library. This library uses BLAS as a backend for computations. So please ensure all [prerequisites](https://nd4j.org/getstarted#Prerequisites) for this are met.

