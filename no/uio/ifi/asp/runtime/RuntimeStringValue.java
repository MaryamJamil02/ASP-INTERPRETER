package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeStringValue extends RuntimeValue {

    String strValue;

    public RuntimeStringValue(String v) {
        strValue = v;
    }

    @Override
    String typeName() {
        return "String";
    }

    public String showInfo() {
        return "\'" + strValue + "\'";
    }

    @Override
    public String toString() {
        return "\'" + strValue + "\'";
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        // string + string
        if (v instanceof RuntimeStringValue) {
            return new RuntimeStringValue(strValue + v.getStringValue("+ operand", where));
        }

        runtimeError("Type error for +.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        // string * int
        if (v instanceof RuntimeIntValue) {
            return new RuntimeStringValue(strValue.repeat((int) v.getIntValue("* operand", where)));
        }

        runtimeError("Type error for *.", where);
        return null;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        // string == string
        if (v instanceof RuntimeStringValue) {
            if (strValue.equals(v.getStringValue("== operand", where))) {
                return new RuntimeBoolValue(true);
            } else {
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
        // string != string
        if (v instanceof RuntimeStringValue) {
            if (!strValue.equals(v.getStringValue("!= operand", where))) {
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
        // string < string
        if (v instanceof RuntimeStringValue) {
            if (strValue.length() < v.getStringValue("< operand", where).length()) {
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
        // string <= string
        if (v instanceof RuntimeStringValue) {
            if (strValue.length() <= v.getStringValue("<= operand", where).length()) {
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
        // string > string
        if (v instanceof RuntimeStringValue) {
            if (strValue.length() > v.getStringValue("> operand", where).length()) {
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
        // string >= string
        if (v instanceof RuntimeStringValue) {
            if (strValue.length() >= v.getStringValue(">= operand", where).length()) {
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
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(strValue.length());
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            String s = "" + strValue.charAt((int) v.getIntValue(strValue, where));
            return new RuntimeStringValue(s);
        }

        return null;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (strValue != "");
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return strValue;
    }
}