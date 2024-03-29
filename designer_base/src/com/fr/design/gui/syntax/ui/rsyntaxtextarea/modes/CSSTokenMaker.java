/* The following code was generated by JFlex 1.4.1 on 2/12/12 12:51 AM */

/*
 * 09/03/2005
 *
 * CSSTokenMaker.java - Token maker for CSS 3 files.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package com.fr.design.gui.syntax.ui.rsyntaxtextarea.modes;

import java.io.*;
import javax.swing.text.Segment;

import com.fr.design.gui.syntax.ui.rsyntaxtextarea.*;


/**
 * This class splits up text into tokens representing a CSS 3 file.  It's
 * written with a few extra internal states so that it can easily be copy
 * and pasted into HTML, PHP, and JSP TokenMakres when it is updated.<p>
 *
 * This implementation was created using
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.1; however, the generated file
 * was modified for performance.  Memory allocation needs to be almost
 * completely removed to be competitive with the handwritten lexers (subclasses
 * of <code>AbstractTokenMaker</code>, so this class has been modified so that
 * Strings are never allocated (via yytext()), and the scanner never has to
 * worry about refilling its buffer (needlessly copying chars around).
 * We can achieve this because RText always scans exactly 1 line of tokens at a
 * time, and hands the scanner this line as an array of characters (a Segment
 * really).  Since tokens contain pointers to char arrays instead of Strings
 * holding their contents, there is no need for allocating new memory for
 * Strings.<p>
 *
 * The actual algorithm generated for scanning has, of course, not been
 * modified.<p>
 *
 * If you wish to regenerate this file yourself, keep in mind the following:
 * <ul>
 *   <li>The generated CSSTokenMaker.java</code> file will contain 2
 *       definitions of both <code>zzRefill</code> and <code>yyreset</code>.
 *       You should hand-delete the second of each definition (the ones
 *       generated by the lexer), as these generated methods modify the input
 *       buffer, which we'll never have to do.</li>
 *   <li>You should also change the declaration/definition of zzBuffer to NOT
 *       be initialized.  This is a needless memory allocation for us since we
 *       will be pointing the array somewhere else anyway.</li>
 *   <li>You should NOT call <code>yylex()</code> on the generated scanner
 *       directly; rather, you should use <code>getTokenList</code> as you would
 *       with any other <code>TokenMaker</code> instance.</li>
 * </ul>
 *
 * @author Robert Futrell
 * @version 0.4
 *
 */

public class CSSTokenMaker extends AbstractJFlexTokenMaker {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** lexical states */
  public static final int CSS_C_STYLE_COMMENT = 5;
  public static final int YYINITIAL = 0;
  public static final int CSS_STRING = 3;
  public static final int CSS_VALUE = 2;
  public static final int CSS_PROPERTY = 1;
  public static final int CSS_CHAR_LITERAL = 4;

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\40\1\61\25\0\1\40\1\63\1\56\1\36\1\52\1\45"+
    "\1\47\1\57\1\43\1\64\1\5\1\51\1\55\1\4\1\6\1\41"+
    "\12\1\1\7\1\37\1\0\1\51\1\60\1\47\1\35\6\46\24\2"+
    "\1\50\1\42\1\50\1\60\1\3\1\0\1\21\1\34\1\15\1\20"+
    "\1\26\1\23\1\33\1\14\1\16\1\2\1\30\1\17\1\27\1\13"+
    "\1\11\1\25\1\2\1\10\1\22\1\12\1\32\1\31\1\53\1\44"+
    "\1\24\1\2\1\54\1\60\1\62\1\51\uff81\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\3\0\1\2\1\3\1\4\2\2\1\5"+
    "\1\6\1\2\1\7\1\10\1\1\1\11\1\12\1\13"+
    "\1\14\1\15\1\14\1\16\1\14\1\17\1\20\1\21"+
    "\1\22\2\1\1\21\1\23\1\1\1\24\1\25\1\21"+
    "\1\26\1\27\1\30\1\31\1\32\1\27\1\33\1\34"+
    "\5\27\1\35\15\0\1\36\1\37\1\40\2\0\1\22"+
    "\3\0\1\22\1\0\1\30\1\41\41\0\1\15\17\0"+
    "\1\42\47\0\1\43";

  private static int [] zzUnpackAction() {
    int [] result = new int[167];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\65\0\152\0\237\0\324\0\u0109\0\u013e\0\u0173"+
    "\0\u01a8\0\u01dd\0\u0212\0\u013e\0\u0247\0\u027c\0\u013e\0\u013e"+
    "\0\u013e\0\u013e\0\u013e\0\u013e\0\u013e\0\u02b1\0\u02e6\0\u013e"+
    "\0\u027c\0\u013e\0\u013e\0\u013e\0\u031b\0\u0350\0\u0385\0\u03ba"+
    "\0\u013e\0\u03ef\0\u013e\0\u013e\0\u0424\0\u013e\0\u0459\0\u048e"+
    "\0\u013e\0\u013e\0\u04c3\0\u013e\0\u013e\0\u04f8\0\u052d\0\u0562"+
    "\0\u0597\0\u05cc\0\u013e\0\u0601\0\u0636\0\u066b\0\u06a0\0\u06d5"+
    "\0\u070a\0\u073f\0\u0774\0\u07a9\0\u07de\0\u0813\0\u0848\0\u087d"+
    "\0\u08b2\0\u08e7\0\u013e\0\u091c\0\u0951\0\u013e\0\u0986\0\u09bb"+
    "\0\u09f0\0\u03ba\0\u0a25\0\u013e\0\u013e\0\u0a5a\0\u0a8f\0\u0ac4"+
    "\0\u0af9\0\u0b2e\0\u0b63\0\u0b98\0\u0bcd\0\u0c02\0\u0c37\0\u0c6c"+
    "\0\u0ca1\0\u0cd6\0\u0d0b\0\u0d40\0\u0d75\0\u0daa\0\u0ddf\0\u0e14"+
    "\0\u0e49\0\u0e7e\0\u0eb3\0\u0ee8\0\u0f1d\0\u0f52\0\u0f87\0\u0fbc"+
    "\0\u0ff1\0\u1026\0\u105b\0\u1090\0\u10c5\0\u10fa\0\u013e\0\u112f"+
    "\0\u1164\0\u1199\0\u11ce\0\u1203\0\u1238\0\u126d\0\u12a2\0\u12d7"+
    "\0\u130c\0\u1341\0\u1376\0\u13ab\0\u13e0\0\u1415\0\u144a\0\u147f"+
    "\0\u14b4\0\u14e9\0\u151e\0\u1553\0\u1588\0\u15bd\0\u15f2\0\u1627"+
    "\0\u165c\0\u1691\0\u16c6\0\u16fb\0\u1730\0\u144a\0\u1765\0\u179a"+
    "\0\u17cf\0\u1804\0\u1839\0\u186e\0\u18a3\0\u18d8\0\u190d\0\u1942"+
    "\0\u1977\0\u19ac\0\u19e1\0\u1a16\0\u1a4b\0\u1a80\0\u1ab5\0\u1aea"+
    "\0\u1b1f\0\u1b54\0\u1b89\0\u1bbe\0\u1bf3\0\u1c28\0\u013e";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[167];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\2\7\5\10\1\11\25\10\1\12\1\13\1\14\1\15"+
    "\1\16\1\7\1\14\1\10\1\7\1\10\1\7\1\14"+
    "\2\17\1\10\1\20\1\21\1\22\1\23\1\17\1\24"+
    "\2\7\1\14\2\25\3\26\1\27\1\25\1\30\25\26"+
    "\3\25\1\15\1\31\2\25\1\26\1\25\1\26\4\25"+
    "\1\26\5\25\1\32\1\33\2\25\1\34\1\35\2\36"+
    "\1\37\1\34\1\21\1\34\25\36\1\34\1\40\1\41"+
    "\1\15\1\42\1\36\1\43\1\36\1\34\1\36\4\34"+
    "\1\36\1\34\1\21\1\22\1\23\1\34\1\44\1\33"+
    "\1\45\1\46\42\47\1\50\13\47\1\51\2\47\1\52"+
    "\3\47\42\53\1\50\14\53\1\54\1\53\1\55\3\53"+
    "\5\56\1\57\6\56\1\60\6\56\1\61\27\56\1\62"+
    "\5\56\1\63\3\56\66\0\4\10\1\0\1\10\1\0"+
    "\25\10\7\0\1\10\1\0\1\10\4\0\1\10\20\0"+
    "\1\64\1\65\1\66\1\67\1\70\1\71\1\72\1\0"+
    "\1\73\1\74\1\75\1\0\1\76\2\0\1\77\2\0"+
    "\1\100\35\0\5\101\1\0\25\101\7\0\1\101\1\0"+
    "\1\101\4\0\1\101\13\0\5\102\1\0\25\102\7\0"+
    "\1\102\1\0\1\102\4\0\1\102\51\0\1\15\31\0"+
    "\1\103\60\0\4\26\3\0\25\26\7\0\1\26\1\0"+
    "\1\26\4\0\1\26\13\0\3\26\3\0\25\26\7\0"+
    "\1\26\1\0\1\26\4\0\1\26\12\0\1\35\4\0"+
    "\1\35\6\0\1\104\1\105\3\0\1\106\2\0\1\107"+
    "\1\110\1\111\15\0\1\106\21\0\3\36\3\0\25\36"+
    "\4\0\2\36\1\43\1\36\1\0\1\36\4\0\1\36"+
    "\12\0\1\35\3\36\3\0\25\36\4\0\2\36\1\43"+
    "\1\36\1\0\1\36\4\0\1\36\12\0\1\112\13\0"+
    "\1\112\2\0\2\112\1\0\1\112\2\0\1\112\5\0"+
    "\1\112\11\0\1\112\20\0\3\36\1\103\2\0\25\36"+
    "\4\0\2\36\1\43\1\36\1\0\1\36\4\0\1\36"+
    "\27\0\1\113\46\0\42\47\1\0\13\47\1\0\2\47"+
    "\1\0\3\47\61\114\1\0\3\114\42\53\1\0\14\53"+
    "\1\0\1\53\1\0\3\53\5\56\1\0\6\56\1\0"+
    "\6\56\1\0\27\56\1\0\5\56\1\0\3\56\41\0"+
    "\1\115\35\0\1\116\64\0\1\117\3\0\1\120\121\0"+
    "\1\121\32\0\1\122\1\0\1\123\10\0\1\124\41\0"+
    "\1\125\66\0\1\126\72\0\1\127\54\0\1\130\1\131"+
    "\63\0\1\132\67\0\1\133\66\0\1\134\2\0\1\135"+
    "\61\0\1\136\63\0\1\137\60\0\1\140\4\0\1\141"+
    "\61\0\1\142\13\0\1\143\53\0\1\144\47\0\4\101"+
    "\1\0\1\101\1\0\25\101\7\0\1\101\1\0\1\101"+
    "\4\0\1\101\12\0\4\102\1\0\1\102\1\0\25\102"+
    "\7\0\1\102\1\0\1\102\4\0\1\102\40\0\1\106"+
    "\50\0\1\106\63\0\1\106\2\0\1\106\26\0\1\106"+
    "\47\0\1\106\14\0\1\106\42\0\1\106\4\0\1\106"+
    "\64\0\1\145\47\0\1\146\77\0\1\147\56\0\1\150"+
    "\120\0\1\151\34\0\1\152\57\0\1\153\74\0\1\154"+
    "\47\0\1\130\72\0\1\155\55\0\1\156\66\0\1\157"+
    "\66\0\1\160\101\0\1\161\61\0\1\162\51\0\1\163"+
    "\64\0\1\164\6\0\1\165\64\0\1\142\54\0\1\166"+
    "\67\0\1\167\57\0\1\170\75\0\1\171\70\0\1\172"+
    "\61\0\1\173\67\0\1\174\64\0\1\175\46\0\1\176"+
    "\103\0\1\147\44\0\1\177\70\0\1\161\62\0\1\200"+
    "\77\0\1\201\65\0\1\202\73\0\1\203\35\0\1\204"+
    "\106\0\1\205\53\0\1\206\77\0\1\157\67\0\1\157"+
    "\43\0\1\202\70\0\1\207\100\0\1\210\54\0\1\165"+
    "\76\0\1\211\42\0\1\212\70\0\1\213\57\0\1\214"+
    "\62\0\1\176\12\0\1\147\103\0\1\215\24\0\2\177"+
    "\5\216\25\177\3\216\1\0\1\177\1\0\1\216\1\177"+
    "\1\216\1\177\3\216\2\177\1\0\1\216\1\0\1\216"+
    "\3\0\2\216\22\0\1\217\53\0\1\220\57\0\1\221"+
    "\106\0\1\130\47\0\1\222\3\0\1\223\1\0\1\224"+
    "\55\0\1\157\104\0\1\225\65\0\1\226\55\0\1\157"+
    "\61\0\1\225\71\0\1\157\52\0\1\225\62\0\1\227"+
    "\115\0\1\177\35\0\1\230\62\0\1\226\65\0\1\222"+
    "\3\0\1\223\72\0\1\231\55\0\1\232\71\0\1\170"+
    "\71\0\1\233\64\0\1\157\50\0\1\234\56\0\1\235"+
    "\64\0\1\236\76\0\1\237\66\0\1\157\65\0\1\240"+
    "\62\0\1\241\57\0\1\242\71\0\1\233\60\0\1\243"+
    "\67\0\1\244\7\0\1\245\62\0\1\246\52\0\1\247"+
    "\65\0\1\226\63\0\1\152\77\0\1\226\37\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[7261];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\1\1\3\0\1\11\4\1\1\11\2\1\7\11"+
    "\2\1\1\11\1\1\3\11\4\1\1\11\1\1\2\11"+
    "\1\1\1\11\2\1\2\11\1\1\2\11\5\1\1\11"+
    "\15\0\2\1\1\11\2\0\1\11\3\0\1\1\1\0"+
    "\2\11\41\0\1\11\17\0\1\1\47\0\1\11";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[167];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /* user code: */

	/**
	 * Internal type denoting a line ending in a CSS property.
	 */
	public static final int INTERNAL_CSS_PROPERTY			= -1;

	/**
	 * Internal type denoting a line ending in a CSS property value.
	 */
	public static final int INTERNAL_CSS_VALUE				= -2;

	/**
	 * Internal type denoting line ending in a CSS double-quote string.
	 * The state to return to is embedded in the actual end token type.
	 */
	public static final int INTERNAL_CSS_STRING				= -(1<<11);

	/**
	 * Internal type denoting line ending in a CSS single-quote string.
	 * The state to return to is embedded in the actual end token type.
	 */
	public static final int INTERNAL_CSS_CHAR				= -(2<<11);

	/**
	 * Internal type denoting line ending in a CSS multi-line comment.
	 * The state to return to is embedded in the actual end token type.
	 */
	public static final int INTERNAL_CSS_MLC				= -(3<<11);

	/**
	 * The state previous CSS-related state we were in before going into a CSS
	 * string, multi-line comment, etc.
	 */
	private int cssPrevState;


	/**
	 * Constructor.  This must be here because JFlex does not generate a
	 * no-parameter constructor.
	 */
	public CSSTokenMaker() {
		super();
	}


	/**
	 * Adds the token specified to the current linked list of tokens as an
	 * "end token;" that is, at <code>zzMarkedPos</code>.
	 *
	 * @param tokenType The token's type.
	 */
	private void addEndToken(int tokenType) {
		addToken(zzMarkedPos,zzMarkedPos, tokenType);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 * @see #addToken(int, int, int)
	 */
	private void addHyperlinkToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so, true);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
	private void addToken(int tokenType) {
		addToken(zzStartRead, zzMarkedPos-1, tokenType);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
	private void addToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param array The character array.
	 * @param start The starting offset in the array.
	 * @param end The ending offset in the array.
	 * @param tokenType The token's type.
	 * @param startOffset The offset in the document at which this token
	 *                    occurs.
	 */
	@Override
	public void addToken(char[] array, int start, int end, int tokenType, int startOffset) {
		super.addToken(array, start,end, tokenType, startOffset);
		zzStartRead = zzMarkedPos;
	}


	/**
	 * Returns <code>true</code> since CSS uses curly braces.
	 *
	 * @return <code>true</code> always.
	 */
	@Override
	public boolean getCurlyBracesDenoteCodeBlocks() {
		return true;
	}


	/**
	 * Returns the first token in the linked list of tokens generated
	 * from <code>text</code>.  This method must be implemented by
	 * subclasses so they can correctly implement syntax highlighting.
	 *
	 * @param text The text from which to get tokens.
	 * @param initialTokenType The token type we should start with.
	 * @param startOffset The offset into the document at which
	 *        <code>text</code> starts.
	 * @return The first <code>Token</code> in a linked list representing
	 *         the syntax highlighted text.
	 */
	public Token getTokenList(Segment text, int initialTokenType, int startOffset) {

		resetTokenList();
		this.offsetShift = -text.offset + startOffset;
		cssPrevState = YYINITIAL; // Shouldn't be necessary

		// Start off in the proper state.
		int state = Token.NULL;
		switch (initialTokenType) {
			case Token.LITERAL_STRING_DOUBLE_QUOTE:
				state = CSS_STRING;
				break;
			case Token.LITERAL_CHAR:
				state = CSS_CHAR_LITERAL;
				break;
			case Token.COMMENT_MULTILINE:
				state = CSS_C_STYLE_COMMENT;
				break;
			case INTERNAL_CSS_PROPERTY:
				state = CSS_PROPERTY;
				break;
			case INTERNAL_CSS_VALUE:
				state = CSS_VALUE;
				break;
			default:
				if (initialTokenType<-1024) {
					int main = -(-initialTokenType & 0xffffff00);
					switch (main) {
						default: // Should never happen
						case INTERNAL_CSS_STRING:
							state = CSS_STRING;
							break;
						case INTERNAL_CSS_CHAR:
							state = CSS_CHAR_LITERAL;
							break;
						case INTERNAL_CSS_MLC:
							state = CSS_C_STYLE_COMMENT;
							break;
					}
					cssPrevState = -initialTokenType&0xff;
				}
				else {
					state = Token.NULL;
				}
		}

		start = text.offset;
		s = text;
		try {
			yyreset(zzReader);
			yybegin(state);
			return yylex();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return new TokenImpl();
		}

	}


	/**
	 * Refills the input buffer.
	 *
	 * @return      <code>true</code> if EOF was reached, otherwise
	 *              <code>false</code>.
	 */
	private boolean zzRefill() {
		return zzCurrentPos>=s.offset+s.count;
	}


	/**
	 * Resets the scanner to read from a new input stream.
	 * Does not close the old reader.
	 *
	 * All internal variables are reset, the old input stream 
	 * <b>cannot</b> be reused (internal buffer is discarded and lost).
	 * Lexical state is set to <tt>YY_INITIAL</tt>.
	 *
	 * @param reader   the new input stream 
	 */
	public final void yyreset(java.io.Reader reader) {
		// 's' has been updated.
		zzBuffer = s.array;
		/*
		 * We replaced the line below with the two below it because zzRefill
		 * no longer "refills" the buffer (since the way we do it, it's always
		 * "full" the first time through, since it points to the segment's
		 * array).  So, we assign zzEndRead here.
		 */
		//zzStartRead = zzEndRead = s.offset;
		zzStartRead = s.offset;
		zzEndRead = zzStartRead + s.count - 1;
		zzCurrentPos = zzMarkedPos = s.offset;
		zzLexicalState = YYINITIAL;
		zzReader = reader;
		zzAtEOF  = false;
	}




  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public CSSTokenMaker(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public CSSTokenMaker(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 134) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  @Override
public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public com.fr.design.gui.syntax.ui.rsyntaxtextarea.Token yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = zzLexicalState;


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 4: 
          { /* Unknown pseudo class */ addToken(Token.DATA_TYPE);
          }
        case 36: break;
        case 11: 
          { addNullToken(); return firstToken;
          }
        case 37: break;
        case 27: 
          { addToken(start,zzStartRead, Token.LITERAL_CHAR); yybegin(cssPrevState);
          }
        case 38: break;
        case 15: 
          { addEndToken(INTERNAL_CSS_PROPERTY); return firstToken;
          }
        case 39: break;
        case 22: 
          { /* End of a function */ addToken(Token.SEPARATOR);
          }
        case 40: break;
        case 6: 
          { addToken(Token.WHITESPACE);
          }
        case 41: break;
        case 2: 
          { /*System.out.println("yyinitial: " + yytext());*/ addToken(Token.IDENTIFIER);
          }
        case 42: break;
        case 32: 
          { start = zzMarkedPos-2; cssPrevState = zzLexicalState; yybegin(CSS_C_STYLE_COMMENT);
          }
        case 43: break;
        case 14: 
          { addToken(Token.OPERATOR); yybegin(CSS_VALUE);
          }
        case 44: break;
        case 13: 
          { addToken(Token.RESERVED_WORD);
          }
        case 45: break;
        case 31: 
          { addToken(Token.VARIABLE);
          }
        case 46: break;
        case 5: 
          { addToken(Token.SEPARATOR);
          }
        case 47: break;
        case 16: 
          { addToken(Token.SEPARATOR); yybegin(YYINITIAL);
          }
        case 48: break;
        case 26: 
          { addToken(start,zzStartRead-1, Token.LITERAL_STRING_DOUBLE_QUOTE); addEndToken(INTERNAL_CSS_STRING - cssPrevState); return firstToken;
          }
        case 49: break;
        case 1: 
          { addToken(Token.IDENTIFIER);
          }
        case 50: break;
        case 19: 
          { addToken(Token.OPERATOR); yybegin(CSS_PROPERTY);
          }
        case 51: break;
        case 9: 
          { start = zzMarkedPos-1; cssPrevState = zzLexicalState; yybegin(CSS_STRING);
          }
        case 52: break;
        case 30: 
          { addToken(Token.REGEX);
          }
        case 53: break;
        case 12: 
          { /*System.out.println("css_property: " + yytext());*/ addToken(Token.IDENTIFIER);
          }
        case 54: break;
        case 17: 
          { /*System.out.println("css_value: " + yytext());*/ addToken(Token.IDENTIFIER);
          }
        case 55: break;
        case 33: 
          { addToken(start,zzStartRead+1, Token.COMMENT_MULTILINE); yybegin(cssPrevState);
          }
        case 56: break;
        case 24: 
          { /* Skip escaped chars. */
          }
        case 57: break;
        case 3: 
          { addToken(Token.DATA_TYPE);
          }
        case 58: break;
        case 10: 
          { start = zzMarkedPos-1; cssPrevState = zzLexicalState; yybegin(CSS_CHAR_LITERAL);
          }
        case 59: break;
        case 35: 
          { addToken(Token.ANNOTATION);
          }
        case 60: break;
        case 25: 
          { addToken(start,zzStartRead, Token.LITERAL_STRING_DOUBLE_QUOTE); yybegin(cssPrevState);
          }
        case 61: break;
        case 20: 
          { int temp = zzMarkedPos - 2;
						  addToken(zzStartRead, temp, Token.FUNCTION);
						  addToken(zzMarkedPos-1, zzMarkedPos-1, Token.SEPARATOR);
						  zzStartRead = zzCurrentPos = zzMarkedPos;
          }
        case 62: break;
        case 8: 
          { addToken(Token.SEPARATOR); yybegin(CSS_PROPERTY);
          }
        case 63: break;
        case 28: 
          { addToken(start,zzStartRead-1, Token.LITERAL_CHAR); addEndToken(INTERNAL_CSS_CHAR - cssPrevState); return firstToken;
          }
        case 64: break;
        case 34: 
          { int temp=zzStartRead; addToken(start,zzStartRead-1, Token.COMMENT_MULTILINE); addHyperlinkToken(temp,zzMarkedPos-1, Token.COMMENT_MULTILINE); start = zzMarkedPos;
          }
        case 65: break;
        case 21: 
          { addEndToken(INTERNAL_CSS_VALUE); return firstToken;
          }
        case 66: break;
        case 29: 
          { addToken(start,zzStartRead-1, Token.COMMENT_MULTILINE); addEndToken(INTERNAL_CSS_MLC - cssPrevState); return firstToken;
          }
        case 67: break;
        case 18: 
          { addToken(Token.LITERAL_NUMBER_DECIMAL_INT);
          }
        case 68: break;
        case 7: 
          { addToken(Token.OPERATOR);
          }
        case 69: break;
        case 23: 
          { 
          }
        case 70: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            switch (zzLexicalState) {
            case CSS_C_STYLE_COMMENT: {
              addToken(start,zzStartRead-1, Token.COMMENT_MULTILINE); addEndToken(INTERNAL_CSS_MLC - cssPrevState); return firstToken;
            }
            case 168: break;
            case YYINITIAL: {
              addNullToken(); return firstToken;
            }
            case 169: break;
            case CSS_STRING: {
              addToken(start,zzStartRead-1, Token.LITERAL_STRING_DOUBLE_QUOTE); addEndToken(INTERNAL_CSS_STRING - cssPrevState); return firstToken;
            }
            case 170: break;
            case CSS_VALUE: {
              addEndToken(INTERNAL_CSS_VALUE); return firstToken;
            }
            case 171: break;
            case CSS_PROPERTY: {
              addEndToken(INTERNAL_CSS_PROPERTY); return firstToken;
            }
            case 172: break;
            case CSS_CHAR_LITERAL: {
              addToken(start,zzStartRead-1, Token.LITERAL_CHAR); addEndToken(INTERNAL_CSS_CHAR - cssPrevState); return firstToken;
            }
            case 173: break;
            default:
            return null;
            }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
