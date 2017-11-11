(ns protobuffalo.dep-loader
  (:use [cemerick.pomegranate :only (add-dependencies)]
        [com.stuartsierra.component :as component]))

(defn- new-cl []
  (clojure.lang.DynamicClassLoader.))

(defn- add-deps! [{:keys [loader repositories]} deps]
  (prn loader)
  (prn repositories)
  (prn deps)
  (add-dependencies :classloader loader
                    :repositories repositories
                    :coordinates deps))

(defprotocol DependencyLoader
  (get-loader [this])
  (add-repositories [this repos])
  (add-deps [this deps])
  (reload [this]))

(defrecord DepLoader [repositories dependencies]
    component/Lifecycle
    (start [this]
      (let [res (assoc this
                       :loader (new-cl)
                       :repositories (into [] repositories)
                       :dependencies (into [] dependencies))]

        (add-deps! res (:dependencies res))
        res))
    (stop [this]
      (assoc this
             :loader nil))
    DependencyLoader
    (get-loader [this] (:loader this))
    (add-repositories [this repositories]
      (into [] (concat (:repositories this) repositories)))
    (add-deps [this deps]
      (do
        (add-deps! this deps)
        (into [] (concat (:dependencies this) deps))))
    (reload [this]
      (let [cl' (new-cl)]
        (add-deps! this (:dependencies this))
        (assoc this :loader cl'))))

(defn new-deploader [repos deps]
  (map->DepLoader {:repositories repos :dependencies deps}))
