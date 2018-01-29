# Java_8

Long before there was object-oriented programming, there were functional programming languages such as Lisp and Scheme, but their benefits
were not much appreciated outside academic circles. Recently, functional programming has risen in importance because it is well suited for concurrent and event-driven (or “reactive”) programming. That doesn’t mean that objects are bad. Instead, the winning strategy is to blend object-oriented and functional programming. This makes sense even if you are not interested in concurrency. For example, collection libraries can be given powerful APIs if the language has a convenient syntax for function expressions.

The principal enhancement in Java 8 is the addition of functional programming constructs to its object-oriented roots. Some of the important features include
1)Lamdba expressions<br>
2)The Stream API<br>
3)Java FX<br>
4)New Date and Time API<br>
5)concurrency enhancements<br>
6)nashorn javascript engine<br>

Lamdba expressions
------------------
A lambda expression is a block of code with parameters. Use a lambda expression whenever you want a block of code executed at a later point in time. Lambda expressions can be converted to functional interfaces.

Let's take an example of a custom comparator, which is used to pass to a sort method later for sorting.

	class LengthComparator implements Comparator<String> {
	   @override
	   public int compare(String first, String second) {
		  return Integer.compare(first.length(), second.length());
	   }
	}

When we want to sort an array of strings using the above comparator at a later time, we write the below line of code:

	Arrays.sort(strings, new LengthComparator());

It means, a block of code is passed to a sort method.Up to now, giving someone a block of code hasn’t been easy in Java. You couldn’t
just pass code blocks around. Java is an object-oriented language, so you had to construct an object belonging to a class that has a method with the desired code.	

Note: you can also write the above logic using anonymous inner class, but it looks more messy.

In Java 8, Lambda expressions are introduced used, which makes passing a block of code easily, without lot of technical details like implementing, overriding etc.

***
Note: In a nutshell, Lambda expressions make instances of anonymous inner classes easier to write and also to read. As we know 90% of the time we read the code than writing, Lambda expressions are easier to read as well.

Writing a lambda expression
---------------------------
The syntax of lambda expression is as below.

	(String first, String second) -> { Integer.compare(first.length(), second.length()); }

There are 2 parts in the above expression.
1)(String first, String second) --> This represents method arguments of compare method.
2) Integer.compare(first.length(), second.length()); --> Block of executable code. We can ignore curly braces if code is only a single line.

Note: We can assign this to a variable like below if we want to reuse the lambda.

	Comparator<String> comparator=(String first, String second) -> Integer.compare(s1.length(), s2.length());

Note:  As we can see, this is much easier to read and write than an anonymous class.

*****
Note: From this we can understand that, Lambda expression is nothing but another way of writing an anonymous innerclass.

Let's see a Runnable lambda expression.
	Runnable runnable = () -> System.out.println("name of the thread: " + Thread.currentThread().getName());

Note: For Runnable, there are no method arguments and we have assigned it to a variable to reuse later.

Questions
---------
What is the type of a lambda expression?
A lambda is a type of functional interface. 

What is a functional interface?
It is an interface with only one abstract method. Also, methods from Object class don't count. For ex, the below one still a functional interface.

	interface MyFunctionalInterface{
		someMethod();
		equals(Object o);
	}

Note: Some interfaces in java library define methods from Object class just to define additional java documentation. 
***
Note: You can supply a lambda expression whenever an object of an interface with a single abstract method is expected. 
***
Note:There are many existing interfaces in Java that encapsulate blocks of code, such as Runnable or Comparator. Lambdas are backwards compatible
with these interfaces.

*****
Is lambda expression an object?
Lambda expression is not created using new operator. It's less overhead on jvm to create lambdas than to create new objects. So, it won't have to be normal life cycle of java object with initilization, construction, garbage collection etc. Also, you cannot invoke any Object class methods like equals, hashcode etc. It is best to think of a lambda expression as a function, not an object, and to accept that it can be passed to a functional interface.

Method references
-----------------
Method references are easier way to write lambda expressions. Consider the below lambda expression:

	String[] names={"tulasi","venki","potti"};
	Arrays.sort(names,(String s1, String s2) -> s1.compareToIgnoreCase(s2));

The above lambda expression can be written with simpler syntax like,

	String[] names={"tulasi","venki","potti"};
	Arrays.sort(names,String::compareToIgnoreCase);
	
As we can see from these examples, the :: operator separates the method name from the name of an object or class. There are three principal cases:
• object::instanceMethod
• Class::staticMethod
• Class::instanceMethod	

In the ﬁrst two cases, the method reference is equivalent to a lambda expression that supplies the parameters of the method. i.e.,
System.out::println is equivalent to x -> System.out.println(x). Similarly, Math::pow is equivalent to (x, y) -> Math.pow(x, y).

In the third case, the ﬁrst parameter becomes the target of the method. For example, 
	
	String::compareToIgnoreCase is the same as (x, y) -> x.compareToIgnoreCase(y).
	
Few more examples of method references
--------------------------------------
1)Consumer<String> c = s -> System.out.println(s) can be written as Consumer<String> c = System.out::println
2)Comparator<Integer> c = (i1,i2) -> Integer.compare(i1,i2) can be written as Comparator<Integer> c = Integer::compare

Constructor references
----------------------
Constructor references are just like method references, except that the name of the method is new. Suppose you have a list of strings.
Then you can turn it into an array of Threads, by calling the Thread constructor on each of the strings, with the following invocation:

	String[] names={"tulasi","venki","potti"};
	List<String> labels=Arrays.asList(names);
	Stream<Thread> stream = labels.stream().map(Thread::new);
	stream.forEach((t) -> System.out.println("Thread name :: " + t.getName())); 
	
Note: We can print using method reference also. For ex, stream.forEach(System.out::println);

You can form constructor references with array types. For example, int[]::new is a constructor reference with one parameter: the length of the array. It is equivalent to the lambda expression x -> new int[x].

*****
Note:Array constructor references are useful to overcome a limitation of Java. It is not possible to construct an array of a generic type T. The expression new T[n] is an error since it would be erased to new Object[n]. That is a problem for library authors. Suppose we want to have an array of Threads. The Stream interface has a toArray method that returns an Object array:
	
	Object[] threads = stream.toArray();

But that is unsatisfactory. The user wants an array of buttons, not objects. The stream library solves that problem with constructor references.

	Thread[] buttons = stream.toArray(Thread[]::new);
	
Functional Interfaces
---------------------
Java 8 provided a rich set of functional interfaces in java.util.function. There are 43 of them. These can be divided into 4 categories.


Category 1
----------
Supplier
--------
Supplier is just a single interface that won't take any object, but provides an object.

	@FunctionalInterface
	public interface Supplier<T>{
		T get();
	}

Consumer
--------
Consumer is just the opposite of Supplier, in which  it takes an object but won't provide any.

	@FunctionalInterface
	public interface Consumer<T>{
		void accept(T t);
	}

Ex: System.out.println() is an example of Consumer.

Category 2
----------
In the 2nd category we have BiConsumer.	It takes two objects and they don't need to be of same type.

	@FunctionalInterface
	public interface BiConsumer<T,U>{
		void accept(T t,U u);
	}

Category 3
----------
In the third category we have predicate, BiPredicate. Predicate takes an object as a parameter and returns a boolean, whereas BiPredicate takes two objects and returns a boolean.

	@FunctionalInterface
	public interface Predicate<T>{
		boolean test(T t);
	}
	
	@FunctionalInterface
	public interface BiPredicate<T,U>{
		boolean test(T t,U u);
	}

Category 4
----------
In the 4th category we have Function and BiFunction. Function takes an object as a parameter and returns an another object. BiFunction takes two objects as arguments and returns an object.
	
	@FunctionalInterface
	public interface Function<T,R>{
		R apply(T t);
	}

	@FunctionalInterface
	public interface BiFunction<T,U,R>{
		R apply(T t,U u);
	}
	
Note: In the Function category we have some special cases like UnaryOperator, In which it takes an argument of a type and returns the object of same type.

	@FunctionalInterface
	public interface UnaryOperator<T> extends Function<T,T>{
	}	

Note: In the BiFunction category also we have some special cases like BinaryOperator, In which it takes two arguments of a type and returns the object of same type.

	@FunctionalInterface
	public interface BinaryOperator<T> extends Function<T,T,T>{
	}

*****
Note: Most of the times, parameter types can be ommitted in lambda expression. For ex in the below statement  compiler automatically deduces the types of parameters s1 and s2.

	Comparator<String> comparator=(s1,s2) -> Integer.compare(s1.length(), s2.length());

Processing Collections with Lambdas
-----------------------------------
	