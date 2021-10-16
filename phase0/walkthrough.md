# Project Abacus

### Scenario Walkthrough

A user inputs an expression like `f(x) = x^2 + 5`. First an empty `Backend.Axes` object is created. This is passed on to `Backend.ExpressionReader`. The `Backend.ExpressionReader` converts `x^2 + 5` into a list `[“x”, “^”, “2”, “+”, “5”]` and passes that on to `Backend.ExpressionCreator`. `Backend.ExpressionCreator` will convert all the variables, operators and numbers into appropriate `Expressions` (`Backend.NumberExpression`, `Backend.VariableExpression`, `Backend.OperatorExpression`) and combines them into an Abstract Syntax Tree, then create a `Backend.FunctionExpression` that stores the function name “f” along with the expression that it evaluates. `Backend.ExpressionReader` will then return this `Backend.FunctionExpression` which will then be added to the `Backend.Axes` object.

The `Renderer` takes an `Backend.Axes` (currently just an `Backend.Expression`), a `Viewpoint`, and other parameters for the desired image (such as size, scales of axes, etc), then generates an int[] array representing pixels of an image. `RendererUseCase` will pass the parameters of the image to `Renderer` which will then convert the pixel array into an image and save it to a file.

