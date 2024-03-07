package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeFloatValue extends RuntimeValue {

    double floatValue;

    public RuntimeFloatValue(double v) {
        floatValue = v;
    }

    @Override
    String typeName() {
        return "Float";
    }

    @Override
    public String showInfo() {
        return toString();
    }

    @Override
    public String toString() {
        return "" + floatValue;
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeFloatValue(floatValue);
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeFloatValue(-floatValue);
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        // float + float
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue +
                    v.getFloatValue("+ operand", where));
        }

        // float + int
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue +
                    v.getIntValue("+ operand", where));
        }
        runtimeError("Type error for +.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        // float - float
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue -
                    v.getFloatValue("- operand", where));
        }

        // float - int
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue -
                    v.getIntValue("- operand", where));
        }
        runtimeError("Type error for -.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        // float * float
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue *
                    v.getFloatValue("* operand", where));
        }

        // float * int
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue *
                    v.getIntValue("* operand", where));
        }
        runtimeError("Type error for *.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        // float / float
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue /
                    v.getFloatValue("/ operand", where));
        }

        // float / int
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue /
                    v.getIntValue("/ operand", where));
        }
        runtimeError("Type error for /.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        // float // float
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(Math.floor(floatValue / v.getFloatValue("// operand", where)));
        }

        // float // int
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(Math.floor(floatValue / v.getIntValue("// operand", where)));
        }
        runtimeError("Type error for //.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        // float % float
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue - v.getFloatValue("% operand", where)
                    * Math.floor(floatValue / v.getFloatValue("% operand", where)));
        }

        // float % int
        else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue - v.getFloatValue("% operand", where)
                    * Math.floor(floatValue / v.getIntValue("% operand", where)));
        }
        runtimeError("Type error for %.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        // float == float
        if (v instanceof RuntimeFloatValue) {
            if (floatValue == v.getFloatValue("== operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }

        // float == int (Converted the int to float)
        else if (v instanceof RuntimeIntValue) {
            if (floatValue == v.getFloatValue("== operand", where)) {
                return new RuntimeBoolValue(true);
            }

            else {
                return new RuntimeBoolValue(false);
            }
        }

        // any == none
        else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(v.getBoolValue("== operand", where));
        }

        runtimeError("Type error for ==.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        // float != float
        if (v instanceof RuntimeFloatValue) {
            if (floatValue != v.getFloatValue("!= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }

        // float != int (Converted the int to float)
        else if (v instanceof RuntimeIntValue) {
            if (floatValue != v.getFloatValue("!= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }

        // any != none
        else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(!v.getBoolValue("!= operand", where));
        }

        runtimeError("Type error for !=.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        // float < float
        if (v instanceof RuntimeFloatValue) {
            if (floatValue < v.getFloatValue("< operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }

        // float < int (Converted the int to float)
        else if (v instanceof RuntimeIntValue) {
            if (floatValue < v.getFloatValue("< operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }

        runtimeError("Type error for <.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        // float <= float
        if (v instanceof RuntimeFloatValue) {
            if (floatValue <= v.getFloatValue("<= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }

        // float <= int (Converted the int to float)
        else if (v instanceof RuntimeIntValue) {
            if (floatValue <= v.getFloatValue("<= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }

        runtimeError("Type error for <=.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        // float > float
        if (v instanceof RuntimeFloatValue) {
            if (floatValue > v.getFloatValue("> operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }

        // float > int (Converted the int to float)
        else if (v instanceof RuntimeIntValue) {
            if (floatValue > v.getFloatValue("> operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }

        runtimeError("Type error for >.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        // float >= float
        if (v instanceof RuntimeFloatValue) {
            if (floatValue >= v.getFloatValue(">= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }

        // float >= int (Converted the int to float)
        else if (v instanceof RuntimeIntValue) {
            if (floatValue >= v.getFloatValue(">= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }

        runtimeError("Type error for >=.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("not operand", where));
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (floatValue != 0.0);
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return floatValue;
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        return (int) floatValue;
    }
}