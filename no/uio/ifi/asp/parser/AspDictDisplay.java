package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import java.util.HashMap;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspDictDisplay extends AspAtom {

    ArrayList<AspStringLiteral> stringLitList = new ArrayList<>();
    ArrayList<AspExpr> exprList = new ArrayList<>();

    AspDictDisplay(int n) {
        super(n);
    }

    static AspDictDisplay parse(Scanner s) {

        enterParser("dict display");

        AspDictDisplay dd = new AspDictDisplay(s.curLineNum());

        skip(s, leftBraceToken);

        if (s.curToken().kind != rightBraceToken) {

            while (true) {
                dd.stringLitList.add(AspStringLiteral.parse(s));
                skip(s, colonToken);
                dd.exprList.add(AspExpr.parse(s));

                if (s.curToken().kind != commaToken) {
                    break;
                }
                skip(s, commaToken);
            }
        }

        skip(s, rightBraceToken);

        leaveParser("dict display");
        return dd;
    }

    @Override
    void prettyPrint() {

        prettyWrite("{");

        int nPrinted = 0;

        for (int i = 0; i < stringLitList.size(); i++) {

            if (nPrinted > 0) {
                prettyWrite(", ");
            }

            stringLitList.get(i).prettyPrint();
            prettyWrite(":");
            exprList.get(i).prettyPrint();

            ++nPrinted;
        }

        prettyWrite("}");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        HashMap<String, RuntimeValue> hashmap = new HashMap<>();

        for (int i = 0; i < stringLitList.size(); i++) {
            hashmap.put(stringLitList.get(i).stringlit, exprList.get(i).eval(curScope));
        }
        return new RuntimeDictValue(hashmap);
    }
}
