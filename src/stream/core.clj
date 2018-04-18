(ns stream.core
  (:require [clojure.java.io :as io]
            [clojure.core.async :refer [thread >!! <!! chan close! go <! go-loop pipe]]))

(defn stream-complete
  ([message] {:status :ok :message message})
  ([] {:status :ok}))

(defn stream-error
  ([err] {:status :error :message (.getMessage err)})
  ([] {:status :error}))


(defn drain
  "Drains a chanel"
  ([chanel action]
   (go-loop []
     (when-let [ln (<! chanel)]
       (action ln)
       (recur))))
  ([chanel] (drain prn)))



(defn stream-transform
  "Takes a transducer and a input chanel, transform the data"
  ([trans c buff-size]
   (let [t (chan buff-size trans)]
     (pipe c t)))
  ([trans c] (stream-transform trans c 15000)))

