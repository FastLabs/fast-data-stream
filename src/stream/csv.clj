(ns stream.csv
  (:require
    [stream.core :refer [stream-complete stream-error]]
    [clojure.data.csv :as csv]
    [clojure.core.async :refer [thread >!! chan close! go <! go-loop >! put! pipeline]]))


(defn- split-csv-line
  "Splits a comma separated in a vector using clojure.data.csv parser"
  [line {:keys [separator]}]
  (first (csv/read-csv line :separator separator)))

(defn- csv-record
  "Returns a function that thakes a line of text, parse it and returns a map in form of a record
  first line is treated as a header for the rest of the lines"
  [options]
  (let [header (atom [])]
    (fn [line]
      (when line
        (let [csv-line (split-csv-line line options)]
          (when-not (compare-and-set! header [] (map keyword csv-line))
            (zipmap @header csv-line)))))))

(defn csv-stream
  [c]
  (let [csv-out (chan)
        csv-fn (csv-record {:separator \,})
        tx (comp (map csv-fn) (filter identity))]
    (pipeline 2 csv-out tx, c)
    csv-out))

