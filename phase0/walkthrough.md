# Project Abacus

### Scenario Walkthrough

A user inputs an expression like `f(x) = x^2 + 5`. First an empty `Axes` object is created. This is passed on to `ExpressionReader`. The `ExpressionReader` converts `x^2 + 5` into a list `[“x”, “^”, “2”, “+”, “5”]` and passes that on to `ExpressionCreator`. `ExpressionCreator` will convert all the variables, operators and numbers into appropriate `Expressions` (`NumberExpression`, `VariableExpression`, `OperatorExpression`) and combines them into an Abstract Syntax Tree, then create a `FunctionExpression` that stores the function name “f” along with the expression that it evaluates. `ExpressionReader` will then return this `FunctionExpression` which will then be added to the `Axes` object.

The `Renderer` takes an `Axes` (currently just an `Expression`), a `Viewpoint`, and other parameters for the desired image (such as size, scales of axes, etc), then generates an int[] array representing pixels of an image. `RendererUseCase` will pass the parameters of the image to `Renderer` which will then convert the pixel array into an image and save it to a file.

