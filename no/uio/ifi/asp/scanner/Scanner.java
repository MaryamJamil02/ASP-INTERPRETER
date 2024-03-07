// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
	private LineNumberReader sourceFile = null;
	private String curFileName;
	private ArrayList<Token> curLineTokens = new ArrayList<>();
	private Stack<Integer> indents = new Stack<>();
	private final int TABDIST = 4;

	// Puts token kinds in an EnumSet for a given range
	EnumSet<TokenKind> getTextTokens = EnumSet.range(andToken, yieldToken);
	EnumSet<TokenKind> getSymbolTokens = EnumSet.range(astToken, semicolonToken);

	public Scanner(String fileName) {
		curFileName = fileName;
		indents.push(0);

		try {
			sourceFile = new LineNumberReader(
					new InputStreamReader(
							new FileInputStream(fileName),
							"UTF-8"));
		} catch (IOException e) {
			scannerError("Cannot read " + fileName + "!");
		}
	}

	private void scannerError(String message) {
		String m = "Asp scanner error";
		if (curLineNum() > 0)
			m += " on line " + curLineNum();
		m += ": " + message;

		Main.error(m);
	}

	// Retrieves the current symbol, which is always the first symbol in curLineTokens.
	// Does not remove the symbol.
	// If necessary, calls readNextLine to read more lines.
	public Token curToken() {
		while (curLineTokens.isEmpty()) {
			readNextLine();
		}
		return curLineTokens.get(0);
	}

	//Removes the current symbol, which is the first symbol in 
	//curLineTokens.
	public void readNextToken() {
		if (!curLineTokens.isEmpty())
			curLineTokens.remove(0);
	}

	//Reads the next line, splits it into symbols, and adds
	//the symbols to curLineToken.
	private void readNextLine() {
		curLineTokens.clear();

		// Read the next line:
		String line = null;

		try {
			line = sourceFile.readLine();

			if (line == null) {
				sourceFile.close();
				sourceFile = null;
			} else {
				Main.log.noteSourceLine(curLineNum(), line);
			}
		} catch (IOException e) {
			sourceFile = null;
			scannerError("Unspecified I/O error!");
		}

		// -- Must be changed in part 1:
		Boolean skipLine = false;
		int indentAmount = 0;

		Token indentToken = new Token(TokenKind.indentToken, curLineNum());
		Token dedentToken = new Token(TokenKind.dedentToken, curLineNum());
		Token newLineToken = new Token(TokenKind.newLineToken, curLineNum());
		Token eofToken = new Token(TokenKind.eofToken, curLineNum());

		//If the line contains something
		if (line != null) {

			//If the line has a comment or indents with a comment("#")
			//Then it skips the line
			if (line.trim().length() == 0 || line.trim().charAt(0) == '#') {
				skipLine = true;
			}

			//Make a new line with white-spaces only (no tabs)
			String newLine = expandLeadingTabs(line);


			if (skipLine == false) {
				indentAmount = findIndent(newLine);
				int indentTop = indents.peek();

				//If indent amount is higher than the top element
				//Then it pushes indentAmount to indents-stack
				if (indentAmount > indentTop) {
					indents.push(indentAmount);
					curLineTokens.add(indentToken);
				}

				//Pop indents if indentAmount is less than indentTop
				else if (indentAmount < indentTop) {
					while (indentAmount < indents.peek()) {
						indents.pop();
						curLineTokens.add(dedentToken);
					}
				}

				else if (indentAmount != indents.peek()) {
					scannerError("Indent error");
				}

				//Checks, creates and adds tokens to curLineTokens
				checkCreateToken(newLine);
			}

			//Terminate line:
			//If the line has not been skipped and is not empty
			if (line.isEmpty() == false && skipLine == false) {
				curLineTokens.add(newLineToken);
			}
		}
		
		//After the last line is read
		//As long as the indents-stack size is greater than 1
		//add a DEDENT-token to curLineTokens
		else {
			while (indents.size() > 1) {
				indents.pop();
				curLineTokens.add(dedentToken);
			}
			curLineTokens.add(eofToken);
		}

		skipLine = false;

		for (Token t : curLineTokens) {
			Main.log.noteToken(t);
		}
	}

	public void checkCreateToken(String line) {

		//Loops through the line
		for (int mainCounter = 0; mainCounter < line.length(); mainCounter++) {
		
			char l = line.charAt(mainCounter); //Fetch character from current index (mainCounter)

			if (l == ' ') {
				continue;
			}

			if (l == '#') {
				break;
			}

			//Checks for string literal
			else if (l == '"' || l == '\'') {
				int startIndex = mainCounter;
				int stopIndex = startIndex + 1;

				//Loops as long as it doesn't meet \' \"
				while (stopIndex < line.length() && (line.charAt(stopIndex) != l)) {
					stopIndex++;
				}

				if (stopIndex < line.length() && line.charAt(stopIndex) == l) {

					String stringLit = line.substring(startIndex + 1, stopIndex); 
					Token stringLitToken = new Token(TokenKind.stringToken, curLineNum());
					stringLitToken.stringLit = stringLit;
					curLineTokens.add(stringLitToken);

					mainCounter = stopIndex;
				}
			}

			//Checks for name-token and text-token
			else if (isLetterAZ(l)) {

				Boolean sameTokenKind = false;
				String wordString = "";
				int counter = mainCounter;

				//Builds up string
				while (counter < line.length() && (isLetterAZ(line.charAt(counter)) || isDigit(line.charAt(counter)))) {
					wordString += line.charAt(counter);
					counter++;
				}

				for (TokenKind tokenkind : getTextTokens) {
					if (tokenkind.image.equals(wordString)) {
						Token token = new Token(tokenkind, curLineNum());
						curLineTokens.add(token);
						token.name = wordString;
						sameTokenKind = true;
						break;
					}
				}

				//If it's not in the list of TokenKind -> then it must be a name-token
				if (sameTokenKind == false) {
					Token nameToken = new Token(TokenKind.nameToken, curLineNum());
					nameToken.name = wordString;
					curLineTokens.add(nameToken);
					sameTokenKind = true;
				}

				//Increases the counter to next character after the processed word
				mainCounter += wordString.length() - 1;

			}

			//Checks for int and float
			else if (isDigit(l)) {

				int indexCounter = mainCounter;
				char currentChar = line.charAt(indexCounter);
				String numberString = "";

				//Builds up the number
				do {
					numberString += currentChar;
					indexCounter++;
					if (indexCounter == line.length()) {
						break;
					}
					currentChar = line.charAt(indexCounter);
				} while (indexCounter < (line.length()) && (currentChar == '.' || isDigit(currentChar)));

				//If number is float
				if (numberString.contains(".")) {
					double floatNumber = Double.parseDouble(numberString);
					Token floatToken = new Token(TokenKind.floatToken, curLineNum());
					floatToken.floatLit = floatNumber;
					curLineTokens.add(floatToken);
				}

				//If number is integer
				else {
					long integerNumber = Long.parseLong(numberString);
					Token integerToken = new Token(TokenKind.integerToken, curLineNum());
					integerToken.integerLit = integerNumber;
					curLineTokens.add(integerToken);
				}

				//Sets mainCounter to the already updated indexCounter value (do-while)
				mainCounter = indexCounter - 1;

			}

			//Checks for symbol-token
			else if (isSymbol(l + "")) {
				
				String symbolString = "";
				int symbolCounter = mainCounter;
				char nextChar = ' ';
				char currentChar = line.charAt(symbolCounter);

				//If its not the last character -> get next character
				if (symbolCounter != line.length() - 1) {
					nextChar = line.charAt(symbolCounter + 1);
				}

				symbolString += currentChar;
				String twoSymbolCheck = symbolString + nextChar;

				//Checks if the symbols is a double symbol
				if (isSymbol(twoSymbolCheck)) {
					symbolString += nextChar;
					mainCounter++;
				}

				for (TokenKind symbolToken : getSymbolTokens) {
					if (symbolToken.image.equals(symbolString)) {
						Token token = new Token(symbolToken, curLineNum());
						curLineTokens.add(token);
						token.name = symbolString;
						break;
					}
				}
			}

			else {
				scannerError("Invalid character: " + l);
			}
		}
	}

	//Checks if it's a symbol
	public boolean isSymbol(String s) {
		for (TokenKind symbolToken : getSymbolTokens) {
			if (s.equals(symbolToken.image)) {
				return true;
			}
		}
		return false;
	}

	//Returns the line number of the current line
	public int curLineNum() {
		return sourceFile != null ? sourceFile.getLineNumber() : 0;
	}

	//Counts the number of leading white-spaces in the current line
	private int findIndent(String s) {
		int indent = 0;

		while (indent < s.length() && s.charAt(indent) == ' ')
			indent++;
		return indent;
	}

	// -- Must be changed in part 1:
	//Converts initial TAB characters to the correct number of white-spaces
	private String expandLeadingTabs(String string) {

		int n = 0;
		int index = 0;
		String newString = "";

		for (char s : string.toCharArray()) {
			if (s == ' ') {
				newString += ' ';
				n++;
			}

			else if (s == '\t') {
				int tabToBlank = TABDIST - (n % TABDIST);
				newString += " ".repeat(tabToBlank);
				n += tabToBlank;
			}

			else {
				newString += string.substring(index);
				break;
			}
			index++;
		}
		return newString;
	}

	private boolean isLetterAZ(char c) {
		return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || (c == '_');
	}

	private boolean isDigit(char c) {
		return '0' <= c && c <= '9';
	}

	public boolean isCompOpr() {
		// -- Must be changed in part 2:
		
		TokenKind k = curToken().kind;

		if (k == lessToken || k == greaterToken ||
			k == doubleEqualToken || k == greaterEqualToken ||
			k == lessEqualToken || k == notEqualToken) {
			return true;
		}

		return false;
	}

	public boolean isFactorPrefix() {
		// -- Must be changed in part 2:
		TokenKind k = curToken().kind;

		if (k == plusToken || k == minusToken) {
			return true;
		}
		return false;
	}

	public boolean isFactorOpr() {
		// -- Must be changed in part 2:
		TokenKind k = curToken().kind;

		if (k == astToken || k == slashToken || 
			k == percentToken || k == doubleSlashToken) {
			return true;
		}
		return false;
	}

	public boolean isTermOpr() {
		// -- Must be changed in part 2:
		TokenKind k = curToken().kind;

		if (k == plusToken || k == minusToken) {
			return true;
		}
		return false;
	}

	public boolean anyEqualToken() {
		for (Token t : curLineTokens) {
			if (t.kind == equalToken)
				return true;
			if (t.kind == semicolonToken)
				return false;
		}
		return false;
	}
}
