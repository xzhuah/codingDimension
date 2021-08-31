* a
	* b
		* e
			* [B](a.b.e) <!-- 2, bad --> <!-- {"path": "a/b/e"} -->
	* c
		* d
			* [A](a.c.d) <!-- 1, good --> <!-- {"path": "a/c/d"} -->
* b
	* b
		* e
			* [B](a.b.e) <!-- 2, bad --> <!-- {"path": "b/b/e"} -->
			* [BB](a.b.e) <!-- 1, middle --> <!-- {"path": "b/b/e"} -->
			* d
				* [BB](a.b.e) <!-- bad --> <!-- {"path": "b/b/e/d"} -->
				* [BB6](a.b.e) <!-- 3, bad --> <!-- {"path": "b/b/e/d"} -->
				* [BB7](a.b.e) <!-- 7 --> <!-- {"path": "b/b/e/d"} -->
				* [BB8](a.b.e) <!-- middle, 7 --> <!-- {"path": "b/b/e/d"} -->
