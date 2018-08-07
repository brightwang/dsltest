

/**
 * Created by brightwang on 2018/8/2.
 */
abstract class TestDsl extends groovy.lang.Script {
    void Write1() {
        def file = new File('/tmp/testdsl1')
        file.write('test1')
    }

    void Write2() {
        def file = new File('/tmp/testdsl2')
        file.write('test2')
    }
}
