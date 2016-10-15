## Scala Gen

[ ![Download](https://api.bintray.com/packages/hntd187/maven/scalagen/images/download.svg) ](https://bintray.com/hntd187/maven/scalagen/_latestVersion)

Docs can be found at: https://hntd187.github.io/scalagen/docs/0.0.2/

Scala gen is a simple library based around being able to generate row based
test data and easily compose a description of that data set. It also currently
includes the ability to write this data to both Csv and Parquet files.

### Add the dependency to your project
```
"com.scalagen" %% "scalagen" % "0.0.2" % Test
```

You then can compose a simple data set like so
```scala
import com.scalagen.data._
val data = IncrementingSource() | YesNoSource()
data.getLine
```

### Available Sources
* GaussianSource (Numbers drawn from a normal distribution)
* BernoulliSource (Booleans drawn from a Bernoulli Trial)
* GenderSource (M or F based on a Bernoulli Trial)
* YesNoSource (Y or N based on a Bernoulli Trial)
* DateSource (Incrementing Dates)
* IncrementingSource (Incrementing integers, like a primary key)
* RandomSource (Random values drawn from a provided set of values)

### Available Writers
* Simple CSV
* Simple Parquet

### Example of a Writer
```scala
import com.scalagen.data._
val data = Csv {
  IncrementingSource() | YesNoSource() | GaussianSource()
}
data.write("data.csv", 10)
data.show()
```

Produces the following
```
╒════════════════════════════════╤════════════════════════════════╤════════════════════════════════╕
│               Id               │            Response            │             Score              │
╞════════════════════════════════╪════════════════════════════════╪════════════════════════════════╡
│               1                │               No               │              4.7               │
│               2                │              Yes               │              0.85              │
│               3                │               No               │             -0.55              │
│               4                │              Yes               │              0.37              │
│               5                │              Yes               │              1.0               │
│               6                │               No               │              2.3               │
│               7                │               No               │              4.0               │
│               8                │              Yes               │              1.4               │
│               9                │              Yes               │              1.1               │
│               10               │              Yes               │              0.92              │
╘════════════════════════════════╧════════════════════════════════╧════════════════════════════════╛
```

You can also alter the format of Doubles and Dates by providing implicit values for those... 
```scala
import com.scalagen.data._
implicit val mc = MathContext.DECIMAL32
implicit val df = DateTimeFormatter.BASIC_ISO_DATE
val data = Csv {
  IncrementingSource() | DateSource(LocalDate.now()) | GaussianSource()
}.withHeaders("Id", "Date", "Score")
data.show()
```

Which produces...
```
╒════════════════════════════════╤════════════════════════════════╤════════════════════════════════╕
│               Id               │              Date              │             Score              │
╞════════════════════════════════╪════════════════════════════════╪════════════════════════════════╡
│               1                │            20161012            │            2.102348            │
│               2                │            20161019            │            1.360111            │
│               3                │            20161026            │            2.734621            │
│               4                │            20161102            │            1.824946            │
│               5                │            20161109            │           0.9169786            │
│               6                │            20161116            │            2.418038            │
│               7                │            20161123            │            2.647185            │
│               8                │            20161130            │            2.361134            │
│               9                │            20161207            │            2.786133            │
│               10               │            20161214            │            3.19905             │
╘════════════════════════════════╧════════════════════════════════╧════════════════════════════════╛
```
