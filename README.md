# codingDimension

I will put a lot of interesting personal projects here, they will be maintained using the [codingDimensionTemplate](https://github.com/xzhuah/codingDimensionTemplate)(upgraded to [codingDimensionTemplate_Guice](https://github.com/xzhuah/codingDimensionTemplate_Guice)), if you are interested in the template, please refer to that [repository](https://github.com/xzhuah/codingDimensionTemplate).

I believe the code in this repo is of good quality, besides using its function, you can also use it for education, learn Java Design Patterns, Java Generic, Optional and Future, synchronization, Guice Dependency injection framework (I upgrade it from not using Guice to using Guice, you can learn a lot from the upgrading history), and so on. I have experience of working in some of the most famous tech companies in the world and I really demostrate some great software engineering ideas in this project, hope you can find them :). Oh, by the way, some of the test is writtern in those main function, I didn't treat the testing very seriously and I think it would be an "won't fix" problem, I am really not a fan to write tests lol, espepcailly for these kind of individual project, unless I need to publish them formally..

I plan to create a video to demo what are those great things in this project, but I just don't have time. I will just summarize them a little bit here

* A common libaray provides some highly reusable functions
* A framework for mongodb IO, I used mongodb for most of the projects currently
* A Crawler framework, easy to use, can easily be adopted to do any kind of crawler tasks.(That's why my crawler classes in this project looks so simple) Future is used
* A WordCloud generator, supper easy to use, support English and Chinese, can draw personalized wordCloud easily
* A Crawler program which can fetch those most popular videos information on bilibili, I will add some analyze logic in the future
* A crawler program which can help you collect stock informatio, I will add some stock analyze logic in the future

One amazing thing about this is: under the organization of codingDimensionTemplate, Every part of the coding is modulized and connected. You can use easily access and adopt the functionality of other modulas from one modulas. Which greatly improved the code reusability.

Things are under active development, a lot of thinsg will come in the future. The framework and template may also be improved. I won't formally release this project and template in short term. This would be a long term project. 


Some of the program need to talk with database, whose schema is not included in the code, so sadlly you may not be able to run those program directly, but you will be able set up a database service and build the schema according to the code. It is very easy to modify the connection parameters so that the program can find your database.
