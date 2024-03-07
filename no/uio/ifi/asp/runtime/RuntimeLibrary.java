// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {
        // -- Must be changed in part 4:

        // float
        assign("float", new RuntimeFunc("float") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "float", where);
                return new RuntimeFloatValue(actualParams.get(0).getFloatValue(name, where));
            }
        });

        // input
        assign("input", new RuntimeFunc("input") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "input", where);
                System.out.println(actualParams.get(0).toString().replaceAll("\'", ""));
                String s = keyboard.nextLine();
                return new RuntimeStringValue(s);
            }
        });

        // int
        assign("int", new RuntimeFunc("int") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "int", where);

                RuntimeValue v = actualParams.get(0);
                long valueInt = 0;

                if (v instanceof RuntimeStringValue) {
                    valueInt = Long.parseLong(v.getStringValue("int", where));
                } else {
                    valueInt = actualParams.get(0).getIntValue(name, where);
                }
                return new RuntimeIntValue(valueInt);
            }
        });

        // len
        assign("len", new RuntimeFunc("len") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "len", where);
                return actualParams.get(0).evalLen(where);
            }
        });

        // print
        assign("print", new RuntimeFunc("print") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                for (int i = 0; i < actualParams.size(); ++i) {
                    if (i > 0)
                        System.out.print(" ");
                    System.out.print(actualParams.get(i).toString().replaceAll("\'", ""));
                }
                System.out.println();

                return new RuntimeNoneValue();
            }
        });

        // range
        assign("range", new RuntimeFunc("range") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 2, "range", where);
                ArrayList<RuntimeValue> rangeList = new ArrayList<>();
                long start = actualParams.get(0).getIntValue(name, where);
                long end = actualParams.get(1).getIntValue(name, where);

                for (long i = start; i < end; i++) {
                    rangeList.add(new RuntimeIntValue(i));
                }
                return new RuntimeListValue(rangeList);
            }
        });

        // str
        assign("str", new RuntimeFunc("str") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "str", where);
                return new RuntimeStringValue(actualParams.get(0).getStringValue(name, where));
            }
        });

    }

    private void checkNumParams(ArrayList<RuntimeValue> actArgs, int nCorrect, String id, AspSyntax where) {
        if (actArgs.size() != nCorrect)
            RuntimeValue.runtimeError("Wrong number of parameters to " + id + "!", where);
    }
}