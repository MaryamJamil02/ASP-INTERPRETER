package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSuite extends AspSyntax {

    AspSmallStmtList smallStmtList;
    ArrayList<AspStmt> stmts = new ArrayList<>();

    protected AspSuite(int n) {
        super(n);
    }

    static AspSuite parse(Scanner s) {

        enterParser("suite");

        AspSuite st = new AspSuite(s.curLineNum());

        if (s.curToken().kind == newLineToken) {

            skip(s, newLineToken);
            skip(s, indentToken);

            while (s.curToken().kind != dedentToken) {
                st.stmts.add(AspStmt.parse(s));
            }

            skip(s, dedentToken);
        }

        else {
            st.smallStmtList = AspSmallStmtList.parse(s);
        }

        leaveParser("suite");
        return st;
    }

    @Override
    void prettyPrint() {

        if (smallStmtList != null) {
            smallStmtList.prettyPrint();
        }

        else {
            prettyWriteLn();
            prettyIndent();

            for (AspStmt stmt : stmts) {
                stmt.prettyPrint();
            }

            prettyDedent();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        RuntimeValue v = null;

        if (smallStmtList != null) {
            v = smallStmtList.eval(curScope);
            return v;
        }

        else {
            for (int i = 0; i < stmts.size(); i++) {
                v = stmts.get(i).eval(curScope);
            }
            return v;
        }
    }
}