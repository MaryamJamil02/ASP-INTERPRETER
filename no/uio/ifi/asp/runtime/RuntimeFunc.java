package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.parser.*;

public class RuntimeFunc extends RuntimeValue {
    AspFuncDef def;
    RuntimeScope defScope;
    String name;

    public RuntimeFunc(AspFuncDef fdef, RuntimeScope scope, String v) {
        def = fdef;
        defScope = scope;
        name = v;
    }

    public RuntimeFunc(String v) {
        name = v;
    }

    @Override
    public String typeName() {
        return "Function";
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actPars, AspSyntax where) {
        
        // check actual parameters-size with formal parameters-size
        checkNumParams(actPars, def.names.size(), name, where);
        RuntimeScope newScope = new RuntimeScope(defScope); // where the function has been declared

        // assign actual parameters to formal parameters
        for (int i = 0; i < def.names.size(); i++) {
            newScope.assign(def.names.get(i).name, actPars.get(i));
        }

        // call the functions suite with the new scope(newScope)
        try {
            def.suite.eval(newScope);
        }

        catch (RuntimeReturnValue rrv) {
            return rrv.value;
        }
        return new RuntimeNoneValue();
    }

    private void checkNumParams(ArrayList<RuntimeValue> actArgs, int nCorrect, String id, AspSyntax where) {
        if (actArgs.size() != nCorrect)
            RuntimeValue.runtimeError("Wrong number of parameters to " + id + "!", where);
    }
}