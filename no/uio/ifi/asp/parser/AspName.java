package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspName extends AspAtom {

    public String name;

    AspName(int n) {
        super(n);
    }

    static AspName parse(Scanner s) {

        enterParser("name");

        AspName n = new AspName(s.curLineNum());
        n.name = s.curToken().name;
        skip(s, nameToken);

        leaveParser("name");
        return n;
    }

    @Override
    void prettyPrint() {
        prettyWrite(name);
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return curScope.find(name, this);
    }
}
