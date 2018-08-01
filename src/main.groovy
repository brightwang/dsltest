import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.SecureASTCustomizer
import org.codehaus.groovy.runtime.MethodClosure
import static org.codehaus.groovy.syntax.Types.*

println('demo')
xml = """<?xml version="1.0" encoding="UTF-8"?>

<project name="{project-name}" basedir="/app" default="build">
    <target name="build:clear">
    </target>
</project>
"""

dsl = new File('./deploy.dsl').text

def binding = new Binding()
def g = new GeneralBuildXml(xml)
def writer = new StringWriter()
binding.setProperty('excludeDir', new MethodClosure(g, 'excludeDir'))
binding.setProperty('excludeFile', new MethodClosure(g, 'excludeFile'))
binding.setProperty('transfer', new MethodClosure(g, 'transfer'))
binding.setVariable('g', g)
binding.setProperty("out", new PrintWriter(writer))
CompilerConfiguration conf = new CompilerConfiguration();
SecureASTCustomizer customizer = new SecureASTCustomizer();
customizer.with {
    closuresAllowed = false // 用户不能写闭包
    methodDefinitionAllowed = false // 用户不能定义方法
    importsWhitelist = [] // 白名单为空意味着不允许导入
    staticImportsWhitelist = [] // 同样，对于静态导入也是这样
    staticStarImportsWhitelist = ['java.lang.Math','java.lang.String','java.lang.Object'] // 只允许 java.lang.Math
    // 用户能找到的令牌列表
    // org.codehaus.groovy.syntax.Types 中所定义的常量
    tokensWhitelist = [1,
                       PLUS,
                       MINUS,
                       MULTIPLY,
                       DIVIDE,
                       MOD,
                       POWER,
                       PLUS_PLUS,
                       MINUS_MINUS,
                       COMPARE_EQUAL,
                       COMPARE_NOT_EQUAL,
                       COMPARE_LESS_THAN,
                       COMPARE_LESS_THAN_EQUAL,
                       COMPARE_GREATER_THAN,
                       COMPARE_GREATER_THAN_EQUAL
    ].asImmutable()
    // 将用户所能定义的常量类型限制为数值类型
    constantTypesClassesWhiteList = [
                                     Integer,
                                     Float,
                                     Long,
                                     Double,
                                     BigDecimal,
            String,
                                     Integer.TYPE,
                                     Long.TYPE,
                                     Float.TYPE,
                                     Double.TYPE,java.lang.Object
    ].asImmutable()
    // 如果接收者是其中一种类型，只允许方法调用
    // 注意，并不是一个运行时类型！
    receiversClassesWhiteList = [
                                 Math,
                                 Integer,
                                 Float,
                                 Double,
                                 Long,
                                 BigDecimal
    ].asImmutable()
}

customizer.setReceiversWhiteList(Arrays.asList(System.class.getName()));
conf.addCompilationCustomizers(customizer);
new GroovyShell(binding, conf).evaluate(dsl)
println(g.getXmlString())