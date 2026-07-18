(ns instrumentmaker.store
  "SSoT for the ISCO-08 7311 precision-instrument workshop
  scheduling/logistics coordination actor (itonami actor pattern,
  ADR-2607121000 / CLAUDE.md Actors section; README's 'Robotics
  premise' — a workshop scheduling/logistics coordination robot
  performs crew scheduling, task/materials-usage/progress-record
  logging and precision-parts/materials supply-order coordination for
  a precision-instrument workshop crew under this advisor/governor
  pair, which never dispatches hardware itself, never performs
  instrument-repair or calibration work itself, and never finalizes
  an instrument-repair/calibration-execution decision or overrides a
  workshop safety officer's judgment — those remain the workshop
  safety officer's exclusive judgment). Modeled on
  cloud-itonami-isco-7222's toolmaker.store for the
  physical-safety-domain shape.

  Domain:

    worker   — a registered precision-instrument workshop crew member
               (:worker-id, :name)
    workshop — a registered precision-instrument workshop
               {:workshop-id :name :max-supply-cost}. `:max-supply-cost`
               is an informational registered ceiling used only to
               decide whether a `:coordinate-supply-order` proposal
               escalates to human sign-off (the governor never blocks
               a within-threshold order outright; it only decides
               commit vs. escalate).
    record   — a committed operating record (a logged
               task/materials-usage/progress entry, a scheduled crew
               operation, a flagged safety concern, or a coordinated
               supply order) — written ONLY via commit-record!.
    ledger   — append-only audit trail, commit or hold.")

(defprotocol Store
  (worker [s worker-id])
  (workshop [s workshop-id])
  (records-of [s worker-id])
  (ledger [s])
  (register-worker! [s worker])
  (register-workshop! [s workshop])
  (commit-record! [s record])
  (append-ledger! [s fact]))

(defrecord MemStore [a]
  Store
  (worker [_ worker-id] (get-in @a [:workers worker-id]))
  (workshop [_ workshop-id] (get-in @a [:workshops workshop-id]))
  (records-of [_ worker-id] (filter #(= worker-id (:worker-id %)) (:records @a)))
  (ledger [_] (:ledger @a))
  (register-worker! [s w]
    (swap! a assoc-in [:workers (:worker-id w)] w) s)
  (register-workshop! [s ws]
    (swap! a assoc-in [:workshops (:workshop-id ws)] ws) s)
  (commit-record! [s record]
    (swap! a update :records (fnil conj []) record) s)
  (append-ledger! [s fact]
    (swap! a update :ledger (fnil conj []) fact) s))

(defn mem-store
  ([] (mem-store {}))
  ([seed] (->MemStore (atom (merge {:workers {} :workshops {} :records [] :ledger []}
                                    seed)))))
