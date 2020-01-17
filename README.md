ESL2
====

Embeddable Scripting Language number 2

Why is it called ESL2? Where's ESL1? ESL1 was called DCTL (Dimodi Cat, The Language), which was a joke on my last name followed by first initial being dimodicat. So, it is Dimodi Cat: some kind of cat. ESL2 is itself a joke, because ESL can also stand for English as a Second Language. And it is the second language. I won't be showing off DCTL, as I tried to put it into a work project. ESL2 is substantially similar, to the point of some classes being completely the same except for the package name. It implements a different enough language, though, that this should be safe.

Where to start? Problem space. In the past I worked on a physics simulation engine that I personally felt needed an internal scripting language. DCTL was a brain-dead simple language that could fill that. The engine used its own markup language to select algorithms and feed those algorithms parameters, but I wanted to put the algorithms into the models, rather than being compiled into the engine. (Of course, the first thing you need is a user base capable of creating algorithms....) The selection of data types and capabilities was tied to amateur programmers trying to solve 3D physics problems. Another highly important consideration was that the engine had a ton of bugs due state being disparate and frequently global. As a result, ESL2 tries to keep the state easy to manage. It is why all of the types are implemented as immutable objects under the covers and there is no global state. It also lead to me giving up on this language. More on that later.

# The Language
This is the language as implemented. Examples are located in the testfiles directory. NOTE: some of the testfiles are expected to fail.

## Data Types:
* Double
* Vector (3-tuple of doubles)
* Matrix (3x3 matrix of doubles)
* Quaternion (to get people away from rotation matrices)
* String
* Array (ordinally indexed Dictionary?)
* Dictionary
* Function Pointer

## Operations

## Operator Precedence
* ()  -- function call
* {}  -- {} is an empty array, { x, y } is syntactic sugar to build an array, { x : y , z : w } is syntactic sugar to build a dictionary
* . []  -- x.y is syntactic sugar for x["y"]
* ^  -- non-associative
* ! -  -- unary negation
* \* / %  -- last is vector cross product
* \+ \-
* = <> > >= < <=
* | &  -- Always short-circuit
* ?:

## Statements
I'm going to mostly use BNF. Hopefully, I'm not doing anything fishy. Note that [] is zero-or-one and {} is zero-to-many.
### Expression
`"call" <expression>`  
To simplify the language, we have specific keywords to find the beginning of a statement. The "first set" of a statement is intentionally small, so that parsing and error recovery is easier. It makes the language a little verbose, though. Sometimes you want to just call a function (for instance, to output a message), and this is how. It will also allow you to add three to the function's result.
### Assignment
`"set" <identifier> { "[" <expression> "]" } "to" <expression>`  
`"set" <identifier> { "." <identifier> } "to" <expression>`  
Assigning to an undefined variable creates it. The parser will catch if you try to create a variable and access it like an array/dictionary in the same statement.
TODO : the parser probably won't catch `set x to x` properly.
### If
`"if" <expression> "then" <statements> { "elseif" <statements> } [ "else" <statements> ] "end"`  
### While
`"while" <expression> [ "call" <identifier> ] "do" <statements> "end"`  
The "call" portion gives the loop a name. See break and continue.
### For
`"for" <variable> "from" <expression> ( "to" | "downto" ) <expression> [ "step" <expression> ] [ "call" <identifier> ] "do" <statements> "end"`  
`"for" <variable> "in" <expression> [ "call" <identifier> ] "do" <statements> "end"`  
The second form iterates over an array or dictionary. When a dictionary is iterated over, the loop control variable is successively set to a two element array of { key, value }.
### Return
`"return" <expression>`
### Select
`"select" <expression> "from"`  
`    [ "also" ] "case" [ ( "above" | "below" ) ] <expression> "is"`  
`    [ "also" ] "case" "from" <expression> "to" <expression> "is"`  
`    [ "also" ] "case" "else" "is"`  
`"end"`  
Select has a lot of forms and does a lot of stuff. Cases are breaking, and "also" is used to have them fall-through. Case else must be the last case.
#### Break
`"break" [ <identifier> ]`  
Breaks out of the current while or for loop, or the named while or for loop. This isn't completely a statement, in that it requires a loop to be in context.
#### Continue
`"continue" [ <identifier> ]`  
#### Function Definition
`"function" <identifier> "(" [ <identifier> { "," <identifier> } ] ")" "is" <statements> "end"`  
This is a pseudo-statement in that it is never executed. Function declaration is static, and a function cannot access the variables of an enclosing function. A mutually-recursive function definition may look like so: `function one () is function two () is <function two statements which call function one> end <function one statements which call function two> end`. All arguments to function calls are pass-by-value, semantically.

Functions are really why I stopped working on this. I wanted to make the code object-oriented, in the javascript sense of dictionaries with methods. Partly, I didn't want to implement closures: capturing the value of a variable in an enclosing scope when the function is dynamically declared. Most importantly, I could not balance two concerns: A) assignment should be the only way to modify the value of a variable, and B) a method ought to be able to modify the object on which it is called. In my view, the code is easier to understand when assignment is the only way to change the value within a variable. Function calls are always pass-by-value. But, the method of an object may have a need to change the underlying object. How do I update some reference holders to the modified dictionary and not others? Or, how do I specify that some function calls have two results, a return value and a modified dictionary, in a syntactically pleasing way? Note that it is not as simple as allowing function calls to return two values: both results may be ephemeral within the current expression. I am also lazy and didn't want to figure out how to cleanly make `self` or `this` work.

## Standard Library
* double Abs (double)  # absolute value
* double Acos (double)  # inverse cosine, result in degrees
* double Asin (double)  # inverse sine, result in degrees
* double Atan (double)  # inverse tangent, result in degrees
* double Atan2 (double, double)  # two-argument inverse tangent, result in degrees
* double Cbrt (double)  # cube root
* double Ceil (double)  # ceiling
* quaternion Conjugate (quaternion)  # complex conjugate
* double ContainsKey (dictionary, value)  # determine if value is a key in dictionary (the language lacks a means to ask for forgiveness)
* double Cos (double)  # cosine, argument in degrees
* double Cosh (double)  # hyperbolic cosine
* string Date ()  # get the current date as mm/dd/yyyy (because I'm a Yank)
* double DegToRad (double)  # degrees to radians
* double Determinant (matrix)  # determinant
* string Error (string)  # log an error string, returns its argument
* double Exp (double)  # raise Euler's constant to some power
* Fatal (string)  # log a fatal message, this function does not return, calling this function stops execution
* double Floor (double)  # floor
* double FromChar (string)  # return the ASCII code of the only character of the string (the string must have only one character)
* value GetIndex (array, double)  # retrieve index double from array
* array GetKeys (dictionary)  # return an array of keys into a dictionary
* double GetScalar (quaternion)  # return the real component of the quaternion
* value GetValue (dictionary, value)  # retrieve the value with key value from the dictionary, die if value is not present (no forgiveness)
* vector GetVec (quaternion)  # return the imaginary components of a quaternion as a vector
* double GetX (vector)  # return the x component of the vector
* double GetY (vector)  # return the y component of the vector
* double GetZ (vector)  # return the z component of the vector
* double Hypot (double, double)  # hypotenuse function : Sqrt(xx + yy) with (I hope) hardening for underflow
* string Info (string)  # log an informational string, returns its argument
* dictionary Insert (dictionary, value, value)  # insert value 2 into dictionary with value 1 as its key and return the modified dictionary (remember, this DOES NOT modify the passed-in dictionary)
* matrix Invert (matrix)  # invert
* quaternion Invert (quaternion)  # invert
* double IsArray (value)  # run-time type identification
* double IsDictionary (value)  # run-time type identification
* double IsDouble (value)  # run-time type identification
* double IsInfinity (double)  # too big?
* double IsMat (value)  # run-time type identification, matrix
* double IsNaN (double)  # is this not a number?
* double IsQuat (value)  # run-time type identification, quaternion
* double IsString (value)  # run-time type identification
* double IsVec (value)  # run-time type identification, vector
* double Length (string)  # length
* double Ln (double)  # logarithme naturel
* double Log (double, double)  # second arg is base (divisor)
* double Magnitude (quaternion)  # should I have used absolute value?
* double Magnitude (vector)  # should I have used absolute value?
* matrix MakeMatColumns (vector, vector, vector)  # make a matrix by interpreting the vectors as columns of the matrix
* matrix MakeMatRows (vector, vector, vector)  # make a matrix by interpreting the vectors as rows of the matrix
* quaternion MakeQuat (double, vector)  # make a quaternion with the given real scalar and vector of imaginary components
* vector MakeVec (double, double, double)  # turn three doubles into a vector
* quaternion MatToQuat (matrix)  # return a quaternion of the same rotation as the given rotation matrix (no error checking)
* double Max (double, double)  # if either is NaN, returns NaN; returns the first argument if comparing positive and negative zero
* double Min (double, double)  # if either is NaN, returns NaN; returns the first argument if comparing positive and negative zero
* array NewArray ()  # returns an empty array
* array NewArrayDefault (double, value)  # returns an array of size double with all indices initialized to value
* dictionary NewDictionary ()  # returns an empty dictionary
* double PI ()  # delicious
* array PopBack (array)  # return a copy of the passed-in array with the last element removed
* array PopFront (array)  # return a copy of the passed-in array with the first element removed
* array PushBack (array, value)  # return a copy of the passed-in array with a size one greater and the last element the passed-in value
* array PushFront (array, value)  # return a copy of the passed-in array with a size one greater and the first element the passed-in value
* matrix QuatToMat (quaternion)  # return a matrix expressing the same rotation as the given quaternion (no error checking)
* double RadToDeg (double)  # radians to degrees
* dictionary RemoveKey (dictionary, value)  # remove the key value or die
* vector RotScaleVecQuat (vector, quaternion)  # rotate vector by quaternion, using the slow method
* vector RotVecQuat (vector, quaternion)  # rotate vector by quaternion, and the quaternion must be a pure rotation
* double Round (double)  # ties to even
* array SetIndex (array, double, value)  # return a copy of array where index double is now value
* double Sin (double)  # sine, argument in degrees
* double Sinh (double)  # hyperbolic sine
* double Size (array)  # size of an array
* double Size (dictionary)  # number of key,value pairs
* double Sqr (double)  # square
* matrix Sqr (matrix)  # square
* quaternion Sqr (quaternion)  # square
* double Sqrt (double)  # square root
* double SubString (string, double, double)  # from character double 1 to character double 2 (java style)
* double Tan (double)  # tangent, argument in degrees
* double Tanh (double)  # hyperbolic tangent
* string Time ()  # get the current time as hh:mm:ss (24 hour)
* string ToCharacter (double)  # return a one character string of the given ASCII code (or die if it isn't ASCII)
* string ToString (double)  # return a string representation of a double: scientific notation, 16 significant figures
* array Transpose (array)  # should be a rectangular array of arrays
* matrix Transpose (matrix)  # transpose
* quaternion Unit (quaternion)  # return a unit quaternion in this direction
* vector Unit (vector)  # return a unit vector in this direction
* double ValueOf (string)  # parse the string into a double value
* string Warn (string)  # log a warning string, returns its argument
* matrix XRotMat (double)  # return a rotation matrix of a rotation on the x axis by an angle in degrees
* quaternion XRotQuat (double)  # return a quaternion of a rotation on the x axis by an angle in degrees
* matrix YRotMat (double)  # return a rotation matrix of a rotation on the y axis by an angle in degrees
* quaternion YRotQuat (double)  # return a quaternion of a rotation on the y axis by an angle in degrees
* matrix ZRotMat (double)  # return a rotation matrix of a rotation on the z axis by an angle in degrees
* quaternion ZRotQuat (double)  # return a quaternion of a rotation on the z axis by an angle in degrees

# The Programmer's View
## Embedding the Language
Where to start? Look at the test drivers. I will update this later. The rationale needs to be added, too.

## Extending the Language
Extensions to the types of the language are only crude. In addition, you cannot extend the operations of the language. My use case for type extension was an opaque object that represented an object being simulated, and a semi-opaque object representing a simulation state. In addition, I only needed to interrogate these objects, never compose them.

Adding functions: my recommendation is to create a class that inherits from ExecutorBuilder, has its own createDefaultFunctions, and calls ExecutorBuilder.createDefaultFunctions immediately. If that function doesn't demonstrate how easy it is to add custom native functions, I should rethink my career. In addition, I frequently reach into the created Executor and replace the logging functions with application-specific ones. I also generally subclass CallingContext to give my functions even more state to access during the function call.

Adding globals: your getters and setters will probably take the CallingContext, cast it to your custom CallingContext subclass, and then get the data from there. Please don't use actual global state.
