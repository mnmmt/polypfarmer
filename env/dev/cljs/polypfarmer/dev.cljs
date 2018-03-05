(ns ^:figwheel-no-load polypfarmer.dev
  (:require
    [polypfarmer.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
