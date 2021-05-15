<template>
	<article class="markdown-body">
		<h1>常用排序算法总结和对比</h1>
		<ol>
			<li>稳定：如果a原本在b前面，而a=b，排序之后a仍然在b的前面；</li>
			<li>不稳定：如果a原本在b的前面，而a=b，排序之后a可能会出现在b的后面；</li>
			<li>内排序：所有排序操作都在内存中完成；</li>
			<li>外排序：由于数据太大，因此把数据放在磁盘中，而排序通过磁盘和内存的数据传输才能进行；</li>
			<li>时间复杂度： 一个算法执行所耗费的时间。</li>
			<li>空间复杂度：运行完一个程序所需内存的大小。</li>
			<li>n: 数据规模</li>
			<li>k: “桶”的个数</li>
			<li>In-place: 不占用额外内存</li>
			<li>Out-place: 占用额外内存</li>
		</ol>
		<h2>各个排序算法时间对比-单位ms</h2>
		<h4>测试一亿条不同数据</h4>
		<pre><code class="language-java hljs"><span class="hljs-comment">//第一次测试</span>
java.util.Arrays总耗时:<span class="hljs-number">12365</span>
com.freedy.dataStructure.sort.ShellSort总耗时:<span class="hljs-number">49918</span>
com.freedy.dataStructure.sort.QuickSort总耗时:<span class="hljs-number">24897</span>
com.freedy.dataStructure.sort.MergeSort总耗时:<span class="hljs-number">21941</span>
<span class="hljs-comment">//第二次测试</span>
java.util.Arrays总耗时:<span class="hljs-number">12347</span>
com.freedy.dataStructure.sort.ShellSort总耗时:<span class="hljs-number">51074</span>
com.freedy.dataStructure.sort.QuickSort总耗时:<span class="hljs-number">20324</span>
com.freedy.dataStructure.sort.MergeSort总耗时:<span class="hljs-number">20220</span>
<span class="hljs-comment">//第三次测试</span>
java.util.Arrays总耗时:<span class="hljs-number">11740</span>
com.freedy.dataStructure.sort.ShellSort总耗时:<span class="hljs-number">50217</span>
com.freedy.dataStructure.sort.QuickSort总耗时:<span class="hljs-number">19434</span>
com.freedy.dataStructure.sort.MergeSort总耗时:<span class="hljs-number">20933</span>
</code></pre>
		<blockquote>
			<p>冒泡、插入、选择排序时间太长就没测了，基数排序由于数据量太大直接堆溢出了</p>
		</blockquote>
		<h4>测试一千万条不同数据</h4>
		<pre><code class="language-java hljs"><span class="hljs-comment">//第一次测试</span>
java.util.Arrays总耗时:<span class="hljs-number">363</span>
com.freedy.dataStructure.sort.ShellSort总耗时:<span class="hljs-number">246</span>
com.freedy.dataStructure.sort.QuickSort总耗时:<span class="hljs-number">315</span>
com.freedy.dataStructure.sort.MergeSort总耗时:<span class="hljs-number">197</span>
com.freedy.dataStructure.sort.RadixSort总耗时:<span class="hljs-number">352</span>
<span class="hljs-comment">//第二次测试</span>
java.util.Arrays总耗时:<span class="hljs-number">305</span>
com.freedy.dataStructure.sort.ShellSort总耗时:<span class="hljs-number">344</span>
com.freedy.dataStructure.sort.QuickSort总耗时:<span class="hljs-number">263</span>
com.freedy.dataStructure.sort.MergeSort总耗时:<span class="hljs-number">257</span>
com.freedy.dataStructure.sort.RadixSort总耗时:<span class="hljs-number">412</span>
<span class="hljs-comment">//第三次测试</span>
java.util.Arrays总耗时:<span class="hljs-number">388</span>
com.freedy.dataStructure.sort.ShellSort总耗时:<span class="hljs-number">426</span>
com.freedy.dataStructure.sort.QuickSort总耗时:<span class="hljs-number">304</span>
com.freedy.dataStructure.sort.MergeSort总耗时:<span class="hljs-number">214</span>
com.freedy.dataStructure.sort.RadixSort总耗时:<span class="hljs-number">364</span>
</code></pre>
		<blockquote>
			<p>冒泡、插入、选择排序时间太长没结果。</p>
		</blockquote>
		<h4>测试十万条不通数据</h4>
		<pre><code class="language-java hljs"><span class="hljs-comment">//第一次测试</span>
java.util.Arrays总耗时:<span class="hljs-number">56</span>
com.freedy.dataStructure.sort.ShellSort总耗时:<span class="hljs-number">68</span>
com.freedy.dataStructure.sort.QuickSort总耗时:<span class="hljs-number">33</span>
com.freedy.dataStructure.sort.MergeSort总耗时:<span class="hljs-number">37</span>
com.freedy.dataStructure.sort.RadixSort总耗时:<span class="hljs-number">83</span>
com.freedy.dataStructure.sort.BubbleSort总耗时:<span class="hljs-number">23153</span>
com.freedy.dataStructure.sort.SelectSort总耗时:<span class="hljs-number">5512</span>
com.freedy.dataStructure.sort.InsertionSort总耗时:<span class="hljs-number">7203</span>
<span class="hljs-comment">//第二次测试</span>
java.util.Arrays总耗时:<span class="hljs-number">64</span>
com.freedy.dataStructure.sort.ShellSort总耗时:<span class="hljs-number">49</span>
com.freedy.dataStructure.sort.QuickSort总耗时:<span class="hljs-number">46</span>
com.freedy.dataStructure.sort.MergeSort总耗时:<span class="hljs-number">32</span>
com.freedy.dataStructure.sort.RadixSort总耗时:<span class="hljs-number">78</span>
com.freedy.dataStructure.sort.BubbleSort总耗时:<span class="hljs-number">22998</span>
com.freedy.dataStructure.sort.SelectSort总耗时:<span class="hljs-number">5551</span>
com.freedy.dataStructure.sort.InsertionSort总耗时:<span class="hljs-number">7247</span>
<span class="hljs-comment">//第三次测试</span>
java.util.Arrays总耗时:<span class="hljs-number">95</span>
com.freedy.dataStructure.sort.ShellSort总耗时:<span class="hljs-number">31</span>
com.freedy.dataStructure.sort.QuickSort总耗时:<span class="hljs-number">43</span>
com.freedy.dataStructure.sort.MergeSort总耗时:<span class="hljs-number">32</span>
com.freedy.dataStructure.sort.RadixSort总耗时:<span class="hljs-number">75</span>
com.freedy.dataStructure.sort.BubbleSort总耗时:<span class="hljs-number">22947</span>
com.freedy.dataStructure.sort.SelectSort总耗时:<span class="hljs-number">5777</span>
com.freedy.dataStructure.sort.InsertionSort总耗时:<span class="hljs-number">7279</span>
</code></pre>
		<p>综上可以看出冒泡、插入、选择实在实在是慢了,以后排序可以直接pass掉了</p>
		<h4>测速代码</h4>
		<pre><code class="language-java hljs"><span class="hljs-keyword">package</span> com.freedy.dataStructure.sort;

<span class="hljs-keyword">import</span> java.lang.reflect.Method;
<span class="hljs-keyword">import</span> java.util.Arrays;
<span class="hljs-keyword">import</span> java.util.Random;

<span class="hljs-comment">/**
 * <span class="hljs-doctag">@author</span> Freedy
 * <span class="hljs-doctag">@date</span> 2021/3/11 14:11
 */</span>
<span class="hljs-keyword">public</span> <span class="hljs-class"><span class="hljs-keyword">class</span> <span class="hljs-title">Test</span> </span>{
    <span class="hljs-comment">//测试数组</span>
    <span class="hljs-keyword">public</span> <span class="hljs-keyword">static</span> <span class="hljs-keyword">int</span>[] TEST_ARRAY;
    <span class="hljs-comment">//最大测试数据</span>
    <span class="hljs-keyword">public</span> <span class="hljs-keyword">final</span> <span class="hljs-keyword">static</span> <span class="hljs-keyword">int</span> MAX_TEST_DATA = <span class="hljs-number">100000</span>;
    <span class="hljs-comment">//是否打印测试数据</span>
    <span class="hljs-keyword">public</span> <span class="hljs-keyword">final</span> <span class="hljs-keyword">static</span> <span class="hljs-keyword">boolean</span> whetherPrint = <span class="hljs-keyword">false</span>;
    <span class="hljs-comment">//是否测试相同数据</span>
    <span class="hljs-keyword">public</span> <span class="hljs-keyword">final</span> <span class="hljs-keyword">static</span> <span class="hljs-keyword">boolean</span> whetherSame = <span class="hljs-keyword">false</span>;

    <span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">static</span> <span class="hljs-keyword">void</span> <span class="hljs-title">main</span><span class="hljs-params">(String[] args)</span> <span class="hljs-keyword">throws</span> Exception </span>{
        sort(<span class="hljs-string">"java.util.Arrays"</span>);
        sort(<span class="hljs-string">"com.freedy.dataStructure.sort.ShellSort"</span>);
        sort(<span class="hljs-string">"com.freedy.dataStructure.sort.QuickSort"</span>, <span class="hljs-number">0</span>, MAX_TEST_DATA - <span class="hljs-number">1</span>);
        sort(<span class="hljs-string">"com.freedy.dataStructure.sort.MergeSort"</span>, <span class="hljs-number">0</span>, MAX_TEST_DATA - <span class="hljs-number">1</span>, <span class="hljs-keyword">new</span> <span class="hljs-keyword">int</span>[MAX_TEST_DATA]);
        sort(<span class="hljs-string">"com.freedy.dataStructure.sort.RadixSort"</span>);
        sort(<span class="hljs-string">"com.freedy.dataStructure.sort.BubbleSort"</span>);
        sort(<span class="hljs-string">"com.freedy.dataStructure.sort.SelectSort"</span>);
        sort(<span class="hljs-string">"com.freedy.dataStructure.sort.InsertionSort"</span>);
    }

    <span class="hljs-comment">/**
     * 测试主方法
     * 利用反射调取各个类的sort算法
     *
     * <span class="hljs-doctag">@param</span> className 测试类的全类名
     */</span>
    <span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">static</span> <span class="hljs-keyword">void</span> <span class="hljs-title">sort</span><span class="hljs-params">(String className, Object... args)</span> </span>{
        <span class="hljs-keyword">try</span> {
            <span class="hljs-keyword">int</span>[] arr = randomArrMaker();
            <span class="hljs-comment">//通过全类名获取字节码对象</span>
            Class&lt;?&gt; sortClass = Class.forName(className);
            Method[] methods = sortClass.getDeclaredMethods();
            <span class="hljs-keyword">boolean</span> isInvoke = <span class="hljs-keyword">false</span>;
            Exception e = <span class="hljs-keyword">null</span>;
            <span class="hljs-keyword">for</span> (Method method : methods) {
                <span class="hljs-keyword">if</span> (method.getName().equals(<span class="hljs-string">"sort"</span>) &amp;&amp;
                        method.getGenericParameterTypes()[<span class="hljs-number">0</span>].
                                getTypeName().equals(<span class="hljs-string">"int[]"</span>)) {
                    method.setAccessible(<span class="hljs-keyword">true</span>);
                    Object[] obj = <span class="hljs-keyword">new</span> Object[args.length + <span class="hljs-number">1</span>];
                    obj[<span class="hljs-number">0</span>] = arr;
                    System.arraycopy(args, <span class="hljs-number">0</span>, obj, <span class="hljs-number">1</span>, args.length);
                    <span class="hljs-comment">//执行方法</span>
                    <span class="hljs-keyword">try</span> {
                        <span class="hljs-keyword">long</span> time = System.currentTimeMillis();
                        method.invoke(<span class="hljs-keyword">null</span>, obj);
                        System.out.println(sortClass.getName() + <span class="hljs-string">"总耗时:"</span> + (System.currentTimeMillis() - time));
                        isInvoke = <span class="hljs-keyword">true</span>;
                        <span class="hljs-keyword">break</span>;
                    } <span class="hljs-keyword">catch</span> (Exception exception) {
                        e=exception;
                    }
                }
            }
            <span class="hljs-keyword">if</span> (!isInvoke) {
                <span class="hljs-keyword">if</span> (e!=<span class="hljs-keyword">null</span>){
                    e.printStackTrace();
                }
                <span class="hljs-keyword">throw</span> <span class="hljs-keyword">new</span> RuntimeException(<span class="hljs-string">"此类中没有符合测试的sort方法,请检查参数是否输入有误."</span>);
            }
            <span class="hljs-keyword">if</span> (whetherPrint) {
                System.out.println(Arrays.toString(arr));
            }
        } <span class="hljs-keyword">catch</span> (Exception e) {
            e.printStackTrace();
        }
    }

    <span class="hljs-comment">/**
     * 生成随机数组
     */</span>
    <span class="hljs-keyword">public</span> <span class="hljs-keyword">static</span> <span class="hljs-keyword">int</span>[] randomArrMaker() {
        Random random = <span class="hljs-keyword">new</span> Random();
        <span class="hljs-keyword">if</span> (whetherSame) {
            <span class="hljs-keyword">if</span> (TEST_ARRAY == <span class="hljs-keyword">null</span>) {
                <span class="hljs-keyword">int</span>[] ints = <span class="hljs-keyword">new</span> <span class="hljs-keyword">int</span>[MAX_TEST_DATA];
                <span class="hljs-keyword">for</span> (<span class="hljs-keyword">int</span> i = <span class="hljs-number">0</span>; i &lt; MAX_TEST_DATA; i++) {
                    ints[i] = random.nextInt(MAX_TEST_DATA * <span class="hljs-number">100</span>);
                }
                TEST_ARRAY = ints;
            }
            <span class="hljs-keyword">return</span> TEST_ARRAY.clone();
        } <span class="hljs-keyword">else</span> {
            <span class="hljs-keyword">int</span>[] ints = <span class="hljs-keyword">new</span> <span class="hljs-keyword">int</span>[MAX_TEST_DATA];
            <span class="hljs-keyword">for</span> (<span class="hljs-keyword">int</span> i = <span class="hljs-number">0</span>; i &lt; MAX_TEST_DATA; i++) {
                ints[i] = random.nextInt(MAX_TEST_DATA * <span class="hljs-number">100</span>);
            }
            <span class="hljs-keyword">return</span> ints;
        }
    }
}
</code></pre>
		<h1></h1>
	</article>
</template>

<script setup lang="ts">
import {loadResource} from "../http";
import {onBeforeRouteLeave} from "vue-router";
import {onMounted} from "vue";

onMounted(()=>{
	loadStyle()
})
/**
 * 加载样式
 */
function loadStyle() {
	const head = document.getElementsByTagName('head')[0];
	const mdLink = document.createElement('link');
	const hjLink = document.createElement('link');
	mdLink.href = loadResource('/css/md.css')
	mdLink.setAttribute("rel", "stylesheet")
	mdLink.setAttribute("class", "md-css")
	hjLink.href = loadResource('/css/hj.css')
	hjLink.setAttribute("rel", "stylesheet")
	hjLink.setAttribute("class", "md-css")
	head.appendChild(mdLink);
	head.appendChild(hjLink);
}
/**
 * 清除样式 防止干扰其他页面
 */
onBeforeRouteLeave((to, from, next) => {
	setTimeout(()=>{
		const cssLink: HTMLCollectionOf<Element> = document.getElementsByClassName("md-css");
		const length = cssLink.length;
		for (let i = 0; i < length; i++) {
			cssLink[0].remove()
		}
	},300)
	next();
})
</script>

<style scoped lang="scss">

</style>