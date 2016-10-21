outp=topictiling_0.0.2

cd ../de.tudarmstadt.langtech.lda
mvn package
mvn install
cd ../de.tudarmstadt.langtech.semantics.segmentation.topictiling
mvn package
mvn dependency:copy-dependencies

mkdir $outp
cp target/*jar $outp
cp -r target/dependency $outp
cp scripts/top*sh $outp
cp scripts/top*bat $outp

cp README.txt $outp