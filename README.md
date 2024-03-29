# Stats

A collection of statistical measures and utilities in Java 8.

Set theoretic measures:

- Jaccard index
- Mutual information
- Overlap coefficient
- Simple Matching coefficient
- Sorensen-Dice coefficient

Rank correlation measures:

- Kendall rank correlation coefficient
- Spearman's rank correlation coefficient

Quantity correlation measures:

- Cosine similarity
- Pearson correlation coefficient

Data binning:

- Linear
- Logarithmic

Other utilities:

- Distribution
- Frequency
- Welford's online algorithm of variance computation

## Using

You can add a dependency from your project as follows:

Using Maven

```xml
<dependency>
    <groupId>gr.james</groupId>
    <artifactId>stats</artifactId>
    <version>0.14</version>
</dependency>
```

Using Gradle

```gradle
implementation 'gr.james:stats:0.14' // Runtime
api            'gr.james:stats:0.14' // Public API
```
