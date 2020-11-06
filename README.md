# codingDimensionTemplate

### What stops us from reusing our own code
 Template for codingDimension Framework. This template is designed for individual Programming learners, Programming enthusiast of all language. Think about how many times did you write a same block of code to solve similar problems? Have you encountered this kind of situation: you want to solve a new problem (for example you want to read/write a csv file) and you remember you have writen those coding for many times before, but you just can't find where those code are. Even if you find them, you have to copy and plaste them into your new project. This template is designed to help you solve this kind of problem. 

### codingDimension's idea to maximize code resuability
As an individual programmer, you may not need so many small projects at all! The reason why you keep writing the same code is because you have too many small projects that are independent from each other. A possible solution is to maintain only one project and maximize the reusability of all the common logic.Every small project could be viewed as a "node" in the big project, and the frequently used common logic could be seprated out and organized together in a "common" space. Your nodes can call those functions in common space, and your node can call funtion in upstream nodes. That is how codingDimension framework manage your code. The benefit is simple and profound: maximize code reusability.

Of couse, you can design you own detailed framework based on the above idea, but codingDimension has its own recommanded way, which can fit in most situations. Use codingDimension earlier and you will soon build up your own code palace  

### codingDimension's Structure.

There is already some simple example code in this template to help illustrate how we should use it. You can basically put any kind of languages into this template: just create a folder for each programming language at the highest level. If you like, you can make a Java node talk with a Python node. Let's look at the java folder, which contains a maven java project. We have two important package to noticed: the common package and nodes package. That demonstrate the idea of codingDimension, we seprate our Java code into two part: in common, we maintain those code with high reusability, in node, we maintain code with low resuability. By default we have 5 sub packages in common: algorithm, framework, io, time, utils. Progammer can put their code into these packages according to the functionality. Also user can add/remove package to meet their own requirement.

 In nodes, we can create new nodes everytime we want to do a new project. We could say: these nodes contains business specific requirement, so their not highly reusable. Each node should solves a set of problems by taking parameter in and returning result out. Some top level interfaces are defined right under exampleNode, and their implementation is maintained in the impl package. We also have constants package to maintain constant, models package to store business specific model class (used to carry data), utils package to store simple utils functions and an integration package. Let me talk about the integration package, each node have data in and data out. The data may comes from other nodes or common layers (using logic in io maybe). You might need to do some preprocess and aggregation on these input data, process them into models that can be consumed by your node. The logic for this part can be put into integration package. Of couse, you don't have to follow this pattern, you can have different pattern for different programming language. But I believe this is a good and generalizable pattern.

### Not only for individuals

I don't expect this framework can be adopted in a large group of professional programmer, but it should be able to support general team projects. For all the project I have done so far, codingDimension can be easily adpoted and improve reusability and maintainability.


### Talk is cheap, show me the code

You might need enough programming background to deeply understand/appreciate the reason behind the design. The simple example coding is far from demonstrating the full power of this framework. Talk is cheap, I decided to use this template to maintain all my future code, there will be a lot of examples in my formal codingDimension repository.