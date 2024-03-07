package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeListValue extends RuntimeValue {

    public ArrayList<RuntimeValue> listValue = new ArrayList<>();

    public RuntimeListValue(ArrayList<RuntimeValue> v) {
        listValue = v;
    }

    @Override
    String typeName() {
        return "List";
    }

    @Override
    public String showInfo() {
        return listValue.toString();
    }

    @Override
    public String toString() {
        return listValue.toString();
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        // list * int (int copies of list)
        if (v instanceof RuntimeIntValue) {
            int repetition = (int) v.getIntValue(" * operand", where);
            ArrayList<RuntimeValue> resultList = new ArrayList<>();

            for (int i = 0; i < repetition; i++) {
                resultList.addAll(listValue);
            }

            return new RuntimeListValue(resultList);
        }

        runtimeError("'*' undefined for " + typeName() + "!", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        // any == none
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }

        runtimeError("Type error for ==.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        // any != none
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }

        runtimeError("Type error for !=.", where);
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("not operand", where));
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(listValue.size());
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (!listValue.isEmpty() && v instanceof RuntimeIntValue) {
            return listValue.get((int) v.getIntValue("subscription", where));
        }
        return null; // Required by the compiler!
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        if (inx instanceof RuntimeIntValue) {
            listValue.set((int) inx.getIntValue("assign element", where), val);
        }
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (listValue.size() != 0);
    }
}