package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspArguments extends AspPrimarySuffix {

    ArrayList<AspExpr> exprList = new ArrayList<>();

    AspArguments(int n) {
        super(n);
    }

    static AspArguments parse(Scanner s) {

        enterParser("arguments");

        AspArguments a = new AspArguments(s.curLineNum());

        skip(s, leftParToken);

        if (s.curToken().kind != rightParToken) {
            while (true) {
                a.exprList.add(AspExpr.parse(s));

                if (s.curToken().kind != commaToken) {
                    break;
                }
                skip(s, commaToken);
            }
        }

        skip(s, rightParToken);

        leaveParser("arguments");
        return a;
    }

    @Override
    void prettyPrint() {

        prettyWrite("(");

        int nPrinted = 0;

        for (AspExpr ae : exprList) {

            if (nPrinted > 0) {
                prettyWrite(", ");
            }

            ae.prettyPrint();
            ++nPrinted;
        }

        prettyWrite(")");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        ArrayList<RuntimeValue> list = new ArrayList<>();

        for (AspExpr e : exprList) {
            list.add(e.eval(curScope));
        }
        return new RuntimeListValue(list);
    }
}
