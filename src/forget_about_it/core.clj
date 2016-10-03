(ns forget-about-it.core
  (:gen-class)
  (:use
   [twitter.oauth]
   [twitter.callbacks]
   [twitter.callbacks.handlers]
   [twitter.api.restful]
   [clojure.pprint])
  (:import
   (twitter.callbacks.protocols SyncSingleCallback)))

(def my-creds (make-oauth-creds ""
                                ""
                                ""
                                ""))

(defn maybe-delete-statuses
  [original-statuses]

  (loop [statuses original-statuses]

    (when (first statuses)

      (let [status      (first statuses)
            response    (statuses-destroy-id
                         :oauth-creds my-creds
                         :params {:id (:id status)
                                  :trim_user true})
            http-status (:status response)]

        (if-not (= 200 (:code http-status))
          (do
            (pprint http-status)
            (throw (RuntimeException. http-status)))
          (recur (rest statuses)))))))

(defn -main
  [& args]
  (do

    (let [first-arg (read-string (first args))
            tweets-to-drop (if (number? first-arg) first-arg 1)]

      (loop [statuses (statuses-user-timeline
                      :oauth-creds my-creds
                      :params {:count 200
                               :trim_user true
                               :exclude_replies false
                               :contibutor_details false
                               :include_rts true})]

        (when (and (= 200 (:code (:status statuses)))
                   (not= tweets-to-drop (count (:body statuses))))

          (do
            (maybe-delete-statuses (drop tweets-to-drop (:body statuses)))
            (pprint (str "Deleted " (count (:body statuses)) " tweets"))
            (recur (statuses-user-timeline
                      :oauth-creds my-creds
                      :params {:count 200
                               :trim_user 200
                               :exclude_replies false
                               :contibutor_details false
                               :include_rts true}))))))
    (pprint "done")
    (shutdown-agents)))
