(ns app.core
  (:require app.mutations
            app.push-receive
            [om.next :as om]
            [untangled.i18n :refer-macros [tr trf]]
            [untangled.client.core :as uc]
            [untangled.client.data-fetch :as df]
            [untangled.websockets.networking :as wn]
            [app.ui :as ui]))

(def initial-state {:ui/react-key    "abc"
                    :current-user    {}
                    :current-channel {}})

(defonce app (atom (uc/new-untangled-client
                     :networking (wn/make-channel-client "/chsk" :global-error-callback (constantly nil))
                     :initial-state initial-state
                     :started-callback (fn [{:keys [reconciler]}]
                                         (df/load-data reconciler [{:app/channels (om/get-query ui/Channel)}] :refresh [:app/channels])
                                         (df/load-data reconciler [{:app/users (om/get-query ui/User)}] :refresh [:app/users])))))
