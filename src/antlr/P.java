// $ANTLR 2.7.5 (20050128): "t.g" -> "P.java"$

package antlr;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

public class P extends antlr.LLkParser       implements PTokenTypes
 {

protected P(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public P(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected P(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public P(TokenStream lexer) {
  this(lexer,1);
}

public P(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final void startRule() throws RecognitionException, TokenStreamException {
		
		Token  n = null;
		
		try {      // for error handling
			n = LT(1);
			match(NAME);
			System.out.println("Hi there, "+n.getText());
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"NAME",
		"NEWLINE"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	
	}
