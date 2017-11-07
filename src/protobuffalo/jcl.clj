(ns protobuffalo.jcl
  (:import
   (org.xeustechnologies.jcl JarClassLoader)
   (java.net URL)))

(defn- jitpack-token-adder [token]
  (if token
    (fn [url]
      (if (= (.getHost url) "jitpack.io")
        (URL. (str (.getProtocol url) "://" token ":@"(.getHost url)  (.getFile url)))
        url))
    identity))

(defn- load-jars [urls]
  (let [jcl (JarClassLoader.)]
    (doseq [url urls]
      (.add jcl url))
    jcl))

(defn create-loader [jitpack-token jars]
  (let [add-token (jitpack-token-adder jitpack-token)
        urls (map #(URL. %) jars)
        urls-w-creds (map add-token urls)]
    (load-jars urls-w-creds)))
