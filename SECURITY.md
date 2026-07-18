# Security Policy

This project handles precision-instrument makers and repairers operating
workflows. Treat vulnerabilities as potentially high impact even when the
demo data is synthetic — this domain's failure modes include physical
worker-safety risk from fine-detail precision work (eye strain, hand
injury) and occasional solder/chemical exposure.

## Do Not Disclose Publicly

Report privately before opening public issues for:

- credential exposure
- real worker, workshop or operator data exposure
- authorization bypass
- Instrument Maker Governor bypass
- audit-ledger tampering
- over-disclosure in reports or exports
- unsafe robot action dispatch
- any path that lets a proposal reach an instrument-repair/
  calibration-execution decision, or a workshop-safety-officer-override
  decision

## Reporting

Use GitHub private vulnerability reporting when available for the repository.
If that is unavailable, contact the repository maintainers through the
cloud-itonami organization before publishing details.

Include:

- affected commit or version
- reproduction steps
- expected and actual behavior
- impact on worker/workshop data, policy enforcement or audit logging
- suggested fix, if known

## Production Guidance

- Store secrets outside Git.
- Keep real worker/workshop/operator data outside this repository.
- Run policy tests before deployment.
- Export and review audit logs regularly.
- Use least privilege for operators and service accounts.
