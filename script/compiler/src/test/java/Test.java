import com.xebisco.yield.script.compiler.*;
import com.xebisco.yield.script.compiler.modifiers.UnknownModifierException;

public class Test {
    public static void main(String[] args) throws ParenthesisLevelException, NotAStatementException, ValueAlreadyExistsException, UnknownModifierException {
        DefaultCompiler compiler = new DefaultCompiler();
        CompilerType type = compiler.compilerType(compiler.sourceSequence("var f = \"test\""), null, new CompilerBank());
        System.out.println(type);
    }
}
