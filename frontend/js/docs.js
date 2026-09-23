/**
 * YATRA ATLAS - ACADEMIC DOCUMENTATION & TECHNICAL DASHBOARD LOGIC
 */

document.addEventListener('DOMContentLoaded', () => {
    // Tab switching
    const tabBtns = document.querySelectorAll('.docs-tab-btn');
    const tabContents = document.querySelectorAll('.docs-tab-content');

    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            tabBtns.forEach(b => b.classList.remove('active'));
            tabContents.forEach(c => c.style.display = 'none');

            btn.classList.add('active');
            const targetId = `tab-${btn.dataset.tab}`;
            const targetContent = document.getElementById(targetId);
            if (targetContent) targetContent.style.display = 'block';

            if (btn.dataset.tab === 'audit-logs') {
                loadAuditLogs();
            }
        });
    });

    // 1. Interactive Algorithm Tester
    const btnRunTest = document.getElementById('btn-run-algo-test');
    if (btnRunTest) {
        btnRunTest.addEventListener('click', async () => {
            const algo = document.getElementById('test-algo-select').value;
            const pattern = document.getElementById('test-pattern-input').value;
            const text = document.getElementById('test-text-input').value;

            btnRunTest.disabled = true;
            btnRunTest.textContent = 'Running...';

            try {
                const res = await API.testAlgorithm(algo, text, pattern);
                document.getElementById('test-result-box').style.display = 'block';
                document.getElementById('test-time-badge').textContent = `${res.executionTimeMicros} µs`;
                document.getElementById('test-output-pre').textContent = res.output;
            } catch (e) {
                alert('Algorithm test execution error: ' + e.message);
            } finally {
                btnRunTest.disabled = false;
                btnRunTest.textContent = 'Run Algorithm Test';
            }
        });
    }

    // 2. Miller-Rabin Primality Analyzer
    const btnRunMR = document.getElementById('btn-run-mr');
    if (btnRunMR) {
        btnRunMR.addEventListener('click', async () => {
            const numStr = document.getElementById('mr-number-input').value.trim();
            const rounds = document.getElementById('mr-rounds-input').value;

            btnRunMR.disabled = true;
            btnRunMR.textContent = 'Testing...';

            try {
                const res = await API.testMillerRabin(numStr, rounds);
                const resBox = document.getElementById('mr-result-box');
                resBox.style.display = 'block';

                document.getElementById('mr-classification-title').textContent = `${res.number} is ${res.classification}`;
                const pill = document.getElementById('mr-pill');
                if (res.isPrime) {
                    pill.style.background = '#dcfce7';
                    pill.style.color = '#166534';
                    pill.textContent = 'Passed Primality Test';
                } else {
                    pill.style.background = '#fee2e2';
                    pill.style.color = '#dc2626';
                    pill.textContent = 'Composite';
                }

                document.getElementById('mr-rounds-val').textContent = `${res.iterationsUsed} rounds`;
                document.getElementById('mr-error-val').textContent = res.isPrime ? `< ${(res.errorProbability * 100).toFixed(6)}%` : '0.0% (Definite)';
                document.getElementById('mr-witness-val').textContent = res.witnessDetails;
            } catch (e) {
                alert('Miller-Rabin error: ' + e.message);
            } finally {
                btnRunMR.disabled = false;
                btnRunMR.textContent = 'Analyze Number';
            }
        });
    }

    // 3. Concurrency Benchmark
    const btnBench = document.getElementById('btn-run-benchmark');
    if (btnBench) {
        btnBench.addEventListener('click', async () => {
            btnBench.disabled = true;
            document.getElementById('bench-loading').style.display = 'inline-block';

            try {
                const b = await API.runBenchmark(1000);
                document.getElementById('benchmark-results-box').style.display = 'block';

                document.getElementById('bench-threads').textContent = b.threadCount;
                document.getElementById('bench-seq-time').textContent = `${b.sequentialTimeMillis} ms`;
                document.getElementById('bench-par-time').textContent = `${b.parallelTimeMillis} ms`;
                document.getElementById('bench-speedup').textContent = `${b.speedupFactor}x Speedup`;
            } catch (e) {
                alert('Benchmark error: ' + e.message);
            } finally {
                btnBench.disabled = false;
                document.getElementById('bench-loading').style.display = 'none';
            }
        });
    }

    // 4. Audit Selection Logs
    const btnRefreshLogs = document.getElementById('btn-refresh-logs');
    if (btnRefreshLogs) {
        btnRefreshLogs.addEventListener('click', loadAuditLogs);
    }
});

async function loadAuditLogs() {
    const tbody = document.getElementById('audit-logs-tbody');
    if (!tbody) return;

    try {
        const logs = await API.getAlgorithmLogs();
        if (logs.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: var(--text-light);">No audit decisions recorded yet. Search or browse to generate logs.</td></tr>';
            return;
        }

        tbody.innerHTML = logs.map(l => {
            const timeStr = new Date(l.timestamp).toLocaleTimeString();
            return `
                <tr>
                    <td style="color: var(--text-light); font-size: 0.85rem;">${timeStr}</td>
                    <td><strong>${l.feature}</strong></td>
                    <td><span class="docs-badge" style="background: #ffedd5; color: #ea580c;">${l.selectedAlgorithm}</span></td>
                    <td style="font-size: 0.85rem;">${l.rationale}</td>
                    <td><code>${l.complexity}</code></td>
                    <td style="font-weight: 700; color: #059669;">${l.timeTakenMicros.toFixed(1)} µs</td>
                </tr>
            `;
        }).join('');
    } catch (e) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: red;">Error fetching audit logs.</td></tr>';
    }
}
