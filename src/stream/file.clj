(ns stream.file
  (:require [stream.core :as stream]
            [clojure.core.async :refer [put! chan thread <!! go <! close!]]
            [clojure.java.io :as io])
  (:import (java.util.zip GZIPInputStream)
           (java.util.zip ZipInputStream)))

(defn line-stream
  [file]
  (let [line-chan (chan)]
    (go (<!
          (thread
            (try
              (with-open [r (io/reader file)]
                (doseq [line (line-seq r)]
                  (put! line-chan line))
                (stream/stream-complete file))
              (catch Exception e (stream/stream-error e)))))
        (close! line-chan))
    line-chan))
;;TODO: move to file


(defn gzip-stream [stream]
  (GZIPInputStream. stream))

(defn zip-stream [stream]
  (ZipInputStream. stream))

(defn drain-to-file!
  "Drains a chanel into a file. Runs in a separate thread"
  [ch file]
  (thread
    (try
      (with-open [writer (io/writer file)]
        (loop []
          (when-let [item (<!! ch)]
            (doto writer
              (.write (str item))
              (.write "\r\n"))
            (recur)))
        (stream/stream-complete file))
      (catch Exception e (stream/stream-error e)))))

;; an example how
'(->> "c:/Temp/position.gz"
      input-stream
      gzip-stream
      line-stream
      drain-to-file!)
