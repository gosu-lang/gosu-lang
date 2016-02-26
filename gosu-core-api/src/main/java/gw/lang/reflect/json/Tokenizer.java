package gw.lang.reflect.json;

import java.io.IOException;
import java.io.Reader;

final class Tokenizer {
  private Reader source;
  private int line;
  private int column;
  private char ch;

  public Tokenizer(Reader source) {
    this.source = source;
    line = 1;
    column = 0;
    nextChar();
  }

  public Token next() {
    Token T;
    eatWhiteSpace();
    switch(ch) {
      case '"':
      case '\'':
        T = consumeString(ch);
        break;
      case '-':
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
        T = consumeNumber();
        break;
      case '{':
        T = newToken(TokenType.LCURLY, "{");
        nextChar();
        break;
      case '}':
        T = newToken(TokenType.RCURLY, "}");
        nextChar();
        break;
      case '[':
        T = newToken(TokenType.LSQUARE, "[");
        nextChar();
        break;
      case ']':
        T = newToken(TokenType.RSQUARE, "]");
        nextChar();
        break;
      case ',':
        T = newToken(TokenType.COMMA, ",");
        nextChar();
        break;
      case ':':
        T = newToken(TokenType.COLON, ":");
        nextChar();
        break;
      case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g':
      case 'h': case 'i': case 'j': case 'k': case 'l': case 'm': case 'n':
      case 'o': case 'p': case 'q': case 'r': case 's': case 't': case 'u':
      case 'v': case 'w': case 'x': case 'y': case 'z': case 'A': case 'B':
      case 'C': case 'D': case 'E': case 'F': case 'G': case 'H': case 'I':
      case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
      case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W':
      case 'X': case 'Y': case 'Z':
        T = consumeConstant();
        break;
      case '\0':
        T = new Token(TokenType.EOF, "EOF", line, column);
        break;
      default:
        T = newToken(TokenType.ERROR, String.valueOf(ch));
        nextChar();
    }
    return T;
  }

  /*
    string = '"' {char} '"' | "'" {char} "'".
    char = unescaped | "\" ('"' | "\" | "/" | "b" | "f" | "n" | "r" | "t" | "u" hex hex hex hex).
    unescaped = any printable Unicode character except '"', "'" or "\".
  */
  private Token consumeString(char quote) {
    StringBuilder sb = new StringBuilder();
    Token T;
    nextChar();
    while(moreChars() && this.ch != quote) {
      if(this.ch == '\\') {
        nextChar();
        switch(this.ch) {
          case '"':
          case '\\':
          case '/':
            sb.append(this.ch);
            nextChar();
            break;
          case 'b':
            sb.append('\b');
            nextChar();
            break;
          case 'f':
            sb.append('\f');
            nextChar();
            break;
          case 'n':
            sb.append('\n');
            nextChar();
            break;
          case 'r':
            sb.append('\r');
            nextChar();
            break;
          case 't':
            sb.append('\t');
            nextChar();
            break;
          case 'u':
            nextChar();
            int u = 0;
            for(int i = 0; i < 4; i++) {
              if(isHexDigit(this.ch)) {
                u = u * 16 + this.ch - '0';
                if(this.ch >= 'A') { // handle hex numbers: 'A' = 65, '0' = 48. 'A'-'0' = 17, 17 - 7 = 10
                  u = u - 7;
                }
              } else {
                nextChar();
                return newToken(TokenType.ERROR, sb.toString());
              }
              nextChar();
            }
            sb.append((char) u);
            break;
          default:
            return newToken(TokenType.ERROR, sb.toString());
        }
      } else {
        sb.append(this.ch);
        nextChar();
      }
    }
    if(this.ch == quote) {
      T = newToken(TokenType.STRING, sb.toString());
    } else {
      T = newToken(TokenType.ERROR, sb.toString());
    }
    nextChar();
    return T;
  }

  /*
    number = [ "-" ] int [ frac ] [ exp ].
    exp = ("e" | "E") [ "-" | "+" ] digit {digit}.
    frac = "." digit {digit}.
    int = "0" |  digit19 {digit}.
    digit = "0" | "1" | ... | "9".
    digit19 = "1" | ... | "9".
  */
  private Token consumeNumber() {
    StringBuilder sb = new StringBuilder();
    Token T;
    boolean err;
    boolean isDouble = false;
    if(ch == '-') {
      sb.append(ch);
      nextChar();
    }
    if(ch != '0') {
      err = consumeDigits(sb);
      if(err) {
        return newToken(TokenType.ERROR, sb.toString());
      }
    } else {
      sb.append(ch);
      nextChar();
    }
    if(ch == '.') {
      isDouble = true;
      sb.append(ch);
      nextChar();
      err = consumeDigits(sb);
      if(err) {
        return newToken(TokenType.ERROR, sb.toString());
      }
    }
    if(ch == 'E' || ch == 'e') {
      isDouble = true;
      sb.append(ch);
      nextChar();
      if(ch == '-') {
        sb.append(ch);
        nextChar();
      } else if(ch == '+') {
        sb.append(ch);
        nextChar();
      }
      err = consumeDigits(sb);
      if(err) {
        return newToken(TokenType.ERROR, sb.toString());
      }
    }
    if(isDouble) {
      T = new Token(TokenType.DOUBLE, sb.toString(), line, column);
    } else {
      T = new Token(TokenType.INTEGER, sb.toString(), line, column);
    }
    return T;
  }

  private boolean consumeDigits(StringBuilder sb) {
    boolean err = false;
    if(isDigit(ch)) {
      while(moreChars() && isDigit(ch)) {
        sb.append(ch);
        nextChar();
      }
    } else {
      err = true;
    }
    return err;
  }

  private boolean isDigit(char ch) {
    return ch >= '0' && ch <= '9';
  }

  private boolean isHexDigit(char ch) {
    return ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f';
  }

  private Token consumeConstant() {
    StringBuilder sb = new StringBuilder();
    Token T;
    do {
      sb.append(ch);
      nextChar();
    } while(moreChars() && (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z'));
    String str = sb.toString();
    TokenType type = Token.constants.get(str);
    if(type == null) {
      T = newToken(TokenType.ERROR, str);
    } else {
      T = newToken(type, str);
    }
    return T;
  }

  private Token newToken(TokenType type, String tokenValue) {
    return new Token(type, tokenValue, line, column);
  }

  private void eatWhiteSpace() {
    while(moreChars() && (ch == '\t' || ch == '\n' || ch == '\r' || ch == ' ')) {
      nextChar();
    }
  }

  private void nextChar() {
    int c;

    try {
      c = source.read();
    } catch (IOException e) {
      c = -1;
    }
    if(c == '\n') {
      column = 1;
      line++;
    }
    else if(c != -1) {
      column++;
    } else {
      c = '\0';
    }
    ch = (char)c;
  }

  private boolean moreChars() {
    return ch != '\0';
  }
}
